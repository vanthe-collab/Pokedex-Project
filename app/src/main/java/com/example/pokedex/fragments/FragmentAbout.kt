package com.example.pokedex.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pokedex.APIService.APIPokemon
import com.example.pokedex.R
import com.example.pokedex.databinding.FragmentAboutBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FragmentAbout : Fragment(R.layout.fragment_about) {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAboutBinding.bind(view)
        val pokemonId = arguments?.getInt("POKE_ID") ?: return
        //gọi lại API
        val retrofit = Retrofit.Builder().baseUrl(APIPokemon.BASE_URL).addConverterFactory(
            GsonConverterFactory.create()
        ).build()

        val apiCall = retrofit.create(APIPokemon::class.java)
        lifecycleScope.launch(Dispatchers.IO) {
            //API lấy thông số
            val pokemonResponse = apiCall.getPokemonStats(pokemonId)
            if (pokemonResponse.isSuccessful && pokemonResponse.body() != null) {
                val weightHeightAb = pokemonResponse.body()!!
                val speciesUrl = weightHeightAb.species.url
                //API lấy tiểu sử
                val pokemonSpecies = apiCall.getPokemonSpeciesByUrl(speciesUrl)

                if (pokemonSpecies.isSuccessful && pokemonSpecies.body() != null) {
                    val species = pokemonSpecies.body()

                    //Description là Tiếng anh và có thể ko có Description
                    val rawDescription =
                        species?.flavor_text_entries?.find { it.language.name == "en" }?.flavor_text
                            ?: "No description available"

                    //dọn dẹp Description
                    val cleanDescription = rawDescription.replace("\n", " ").replace("\u000c", " ")
                    withContext(Dispatchers.Main) {
                        binding.tvDescription.text = cleanDescription
                        binding.tvWeight.text = "${(weightHeightAb?.weight ?: 0) / 10.0} kg"
                        binding.tvHeight.text = "${(weightHeightAb?.height ?: 0) / 10.0} m"
                        binding.tvAbility.text =
                            weightHeightAb.abilities.joinToString(", ") { it.ability.name }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}