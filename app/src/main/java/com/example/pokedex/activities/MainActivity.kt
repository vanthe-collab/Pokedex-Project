package com.example.pokedex.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.APIService.Apipokemon
import com.example.pokedex.Model.DataPokemon
import com.example.pokedex.adapters.PokemonAdapter
import com.example.pokedex.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.pokedex.R

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentOffset = 0
    private var limit = 20
    private var isLoading = false //có đang bận tải ko, tránh trường hợp user vuôt nhanh quá

    private var listSumary = ArrayList<DataPokemon>()
    private lateinit var adapter: PokemonAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //hiển thị màu gốc đã có cho các icons -> navigation
        binding.bottomNavigation.itemIconTintList = null

        getAPI()
        ScrollDown()

        // --- BẮT SỰ KIỆN MENU TRƯỢT ---
        binding.topAppBar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    fun getAPI() {
        isLoading = true
        val retrofit = Retrofit.Builder().baseUrl(Apipokemon.BASE_URL).addConverterFactory(
            GsonConverterFactory.create()
        ).build()

        val apiPokemon = retrofit.create(Apipokemon::class.java)

        //gọi courountines xử lý bất đồng bộ
        lifecycleScope.launch(Dispatchers.IO) {
            val responseList = apiPokemon.getPokemonList(limit, currentOffset)
            if (responseList.isSuccessful && responseList.body() != null) {
                val listDataRaw = responseList.body()!!.results
                val listDataFormated = listDataRaw.map { item ->
                    async {
                        val responseDetail = apiPokemon.getPokemonDetail(item.url)
                        if (responseDetail.isSuccessful && responseDetail.body() != null) {
                            val detail = responseDetail.body()!!

                            DataPokemon(
                                detail.id,
                                detail.name.replaceFirstChar { it.uppercase() },
                                detail.getSimpleTypes(),
                                detail.getOfficialImageUrl()
                            )

                        } else {
                            //nếu có pokemon nào bị lỗi thì gán băng null
                            null
                        }
                    }
                }.awaitAll() //làm xong tất cả rồi gom tất cả Data thành 1 cục

                val listNewPokemon = listDataFormated.filterNotNull()
                withContext(Dispatchers.Main) {
                    val oldSize = listSumary.size
                    listSumary.addAll(listNewPokemon)
                    if (currentOffset == 0) {
                        adapter = PokemonAdapter(listSumary)
                        binding.rvPokemonList.layoutManager = GridLayoutManager(
                            this@MainActivity, 2,
                            GridLayoutManager.VERTICAL, false
                        )
                        binding.rvPokemonList.adapter = adapter
                    } else {
                        adapter.notifyItemRangeInserted(oldSize, listNewPokemon.size)
                    }
                    isLoading = false
                }
            }
        }


    }

    //hàm bắt sự kiện cuộn xuống đáy màn hình
    fun ScrollDown() {
        binding.rvPokemonList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) { //kiem tra co cuon xuong hay ko
                    val layoutManger = recyclerView.layoutManager as GridLayoutManager
                    val visibleItemCount = layoutManger.childCount
                    val totalItemCount = layoutManger.itemCount
                    val pasVisibleItems = layoutManger.findFirstVisibleItemPosition()
                    if (!isLoading) {
                        if ((visibleItemCount + pasVisibleItems) >= totalItemCount) {
                            currentOffset += limit
                            getAPI()
                        }
                    }
                }
            }
        })
    }


}