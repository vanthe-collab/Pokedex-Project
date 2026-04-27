package com.example.pokedex.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.APIService.APIPokemon
import com.example.pokedex.Model.ChainLink
import com.example.pokedex.Model.EvolutionItem
import com.example.pokedex.R
import com.example.pokedex.adapters.PokemonEvolutionChainAdapter
import com.example.pokedex.databinding.FragmentEvolutionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FragmentEvolutions : Fragment(R.layout.fragment_evolution) {
    private var _binding: FragmentEvolutionBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEvolutionBinding.bind(view)
        val pokemonId = arguments?.getInt("POKE_ID") ?: 0
        val retrofit = Retrofit.Builder().baseUrl(APIPokemon.BASE_URL).addConverterFactory(
            GsonConverterFactory.create()
        ).build()
        val APIPokemon = retrofit.create(APIPokemon::class.java)
        lifecycleScope.launch(Dispatchers.IO) {
            val baseStatResponse = APIPokemon.getPokemonStats(pokemonId)
            if (baseStatResponse.isSuccessful && baseStatResponse.body() != null) {

                val speciesUrl = baseStatResponse.body()!!.species.url
                val speciesResponse = APIPokemon.getPokemonSpeciesByUrl(speciesUrl)

                if (speciesResponse.isSuccessful && speciesResponse.body() != null) {
                    val speciesData = speciesResponse.body()!!
                    val evolutionResponse =
                        APIPokemon.getEvolutionChain(speciesData.evolution_chain.url)
                    if (evolutionResponse.isSuccessful && evolutionResponse.body() != null) {
                        val chainData = evolutionResponse.body()!!.chain

                        val evoList = mutableListOf<EvolutionItem>()
                        var currentChain: ChainLink? = chainData

                        while (currentChain != null) {
                            val pokeName =
                                currentChain.species.name.replaceFirstChar { it.uppercase() }
                            val speciesUrl = currentChain.species.url

                            val pokeID = speciesUrl.split("/").dropLast(1).last()

                            val imageUrl =
                                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$pokeID.png"

                            evoList.add(EvolutionItem(pokeName, imageUrl))
                            currentChain = currentChain.evolves_to.firstOrNull()
                        }
                        withContext(Dispatchers.Main) {
                            val adapter = PokemonEvolutionChainAdapter(evoList)
                            binding.rvEvolution.layoutManager = LinearLayoutManager(
                                view.context,
                                LinearLayoutManager.HORIZONTAL, false
                            )
                            binding.rvEvolution.adapter = adapter
                        }
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