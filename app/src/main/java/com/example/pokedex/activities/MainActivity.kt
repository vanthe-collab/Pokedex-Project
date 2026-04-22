package com.example.pokedex.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.APIService.Apipokemon
import com.example.pokedex.Model.DataPokemon
import com.example.pokedex.Model.PokemonResponse
import com.example.pokedex.adapters.PokemonAdapter
import com.example.pokedex.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //hiển thị màu gốc đã có cho các icons -> navigation
        binding.bottomNavigation.itemIconTintList = null

        getAPI()
    }

    fun getAPI() {
        val retrofit = Retrofit.Builder().baseUrl(Apipokemon.BASE_URL).addConverterFactory(
            GsonConverterFactory.create()
        ).build()

        val apiPokemon = retrofit.create(Apipokemon::class.java)

        //gọi courountines xử lý bất đồng bộ
        lifecycleScope.launch(Dispatchers.IO) {
            val responeList = apiPokemon.getPokemonList(100, 0)

            if (responeList.isSuccessful && responeList.body() != null) {
                val listDataRaw = responeList.body()!!.results
                val listDataFormated = ArrayList<DataPokemon>()
                for (item in listDataRaw) {
                    val responseDetail = apiPokemon.getPokemonDetail(item.url)

                    if (responseDetail.isSuccessful && responseDetail.body() != null) {
                        val detail = responseDetail.body()!!

                        val PokemonGreat = DataPokemon(
                            detail.id,
                            detail.name.replaceFirstChar { it.uppercase() },
                            detail.getSimpleTypes(),
                            detail.getOfficialImageUrl()
                        )
                        listDataFormated.add(PokemonGreat)
                    }
                }
                withContext(Dispatchers.Main) {
                    val adapter = PokemonAdapter(listDataFormated)
                    binding.rvPokemonList.layoutManager = GridLayoutManager(
                        this@MainActivity, 2,
                        GridLayoutManager.VERTICAL, false
                    )
                    binding.rvPokemonList.adapter = adapter
                }
            }
        }


    }
}