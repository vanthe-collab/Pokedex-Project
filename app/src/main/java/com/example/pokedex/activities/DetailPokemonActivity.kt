package com.example.pokedex.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.pokedex.Model.DataPokemon
import com.example.pokedex.R
import com.example.pokedex.adapters.PokemonPagerAdapter
import com.example.pokedex.databinding.ActivityDetailPokemonBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.collections.firstOrNull
import kotlin.collections.isNotEmpty
import kotlin.let
import kotlin.text.lowercase
import kotlin.text.replaceFirstChar
import kotlin.text.uppercase

class DetailPokemonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPokemonBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }
        displayDataPokemon()
    }

    //Load ảnh và hệ
    fun setUpImagesTypes(dataPokemon: DataPokemon, ivPokemon: ImageView) {
        Glide.with(this).load(dataPokemon.imageUrl).into(ivPokemon)

        if (dataPokemon.types.isNotEmpty()) {
            binding.tvType1.text = dataPokemon.types[0].replaceFirstChar { it.uppercase() }
            binding.tvType1.visibility = View.VISIBLE
        }

        if (dataPokemon.types.size > 1) {
            binding.tvType2.text = dataPokemon.types[1].replaceFirstChar { it.uppercase() }
            binding.tvType2.visibility = View.VISIBLE
        } else {
            binding.tvType2.visibility = View.GONE
        }
    }

    //đổi màu theo hệ
    fun setBackGroundTypes(dataPokemon: DataPokemon, rootLayout: ConstraintLayout) {
        val primaryType = dataPokemon.types.firstOrNull() ?: "normal"

        val colorID = when (primaryType.lowercase()) {
            "fire" -> R.color.poke_fire
            "water" -> R.color.poke_water
            "grass" -> R.color.poke_grass
            "electric" -> R.color.poke_electric
            "ice" -> R.color.poke_ice
            "fighting" -> R.color.poke_fighting
            "poison" -> R.color.poke_poison
            "ground" -> R.color.poke_ground
            "flying" -> R.color.poke_flying
            "psychic" -> R.color.poke_psychic
            "rock" -> R.color.poke_rock
            "bug" -> R.color.poke_bug
            "ghost" -> R.color.poke_ghost
            "dragon" -> R.color.poke_dragon
            "dark" -> R.color.poke_dark
            "steel" -> R.color.poke_steel
            "fairy" -> R.color.poke_fairy
            else -> R.color.poke_normal
        }
        val mauThucTe = ContextCompat.getColor(this, colorID)
        rootLayout.setBackgroundColor(mauThucTe)

    }

    //hiển thị các dữ liệu data Pokemon
    fun displayDataPokemon() {
        val pokemon = intent.getSerializableExtra("POKE_DATA") as? DataPokemon
        pokemon?.let { data ->
            binding.tvDetailName.text = data.name.replaceFirstChar { it.uppercase() }
            binding.tvDetailId.text = "#${data.id}"

            setUpImagesTypes(pokemon, binding.ivDetailPokemon)
            setBackGroundTypes(pokemon, binding.detailRootLayout)
            viewPager2AndTabLayouts(pokemon.id)
        }
    }

    fun viewPager2AndTabLayouts(pokemonId: Int) {
        val pagerAdapter = PokemonPagerAdapter(this, pokemonId)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            //đặt tên từng tab
            tab.text = when (position) {
                0 -> "About"
                1 -> "Base Stats"
                2 -> "Evolution"
                else -> ""
            }
        }.attach()
    }
}