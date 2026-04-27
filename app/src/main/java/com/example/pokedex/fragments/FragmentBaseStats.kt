package com.example.pokedex.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pokedex.APIService.APIPokemon
import com.example.pokedex.R
import com.example.pokedex.databinding.FragmentBaseStatsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FragmentBaseStats : Fragment(R.layout.fragment_base_stats) {
    private var _binding: FragmentBaseStatsBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentBaseStatsBinding.bind(view)

        val pokemonID = arguments?.getInt("POKE_ID") ?: return

        lifecycleScope.launch(Dispatchers.IO) {
            val retrofit = Retrofit.Builder().baseUrl(APIPokemon.BASE_URL).addConverterFactory(
                GsonConverterFactory.create()
            ).build()
            val apiStats = retrofit.create(APIPokemon::class.java)
            val pokemonResponse = apiStats.getPokemonStats(pokemonID)
            if (pokemonResponse.isSuccessful) {
                val listStat = pokemonResponse.body()?.let { listStat ->
                    val mapStat = listStat.stats.associate { it.stat.name to it.base_stat }

                    val hpValue = mapStat["hp"] ?: 0
                    val defenseValue = mapStat["defense"] ?: 0
                    val attackValue = mapStat["attack"] ?: 0
                    val spAtkValue = mapStat["special-attack"] ?: 0
                    val spDefValue = mapStat["special-defense"] ?: 0
                    val speedValue = mapStat["speed"] ?: 0
                    val total = listStat.stats.sumOf { it.base_stat }

                    withContext(Dispatchers.Main) {
                        binding.tvStatHP.text = hpValue.toString()
                        binding.tvStatAT.text = attackValue.toString()
                        binding.tvStatDef.text = defenseValue.toString()
                        binding.tvStatSPAtk.text = spAtkValue.toString()
                        binding.tvStatSPDef.text = spDefValue.toString()
                        binding.tvStatSpeed.text = speedValue.toString()
                        binding.tvStatTotal.text = total.toString()

                        setEffectProgressBar(binding.pbStatHP, hpValue)
                        setEffectProgressBar(binding.pbStatDef, defenseValue)
                        setEffectProgressBar(binding.pbStatAT, attackValue)
                        setEffectProgressBar(binding.pbStatSPAtk, spAtkValue)
                        setEffectProgressBar(binding.pbStatSPDef, spDefValue)
                        setEffectProgressBar(binding.pbStatSpeed, speedValue)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun setEffectProgressBar(progressBar: ProgressBar, value: Int) {
        ObjectAnimator.ofInt(
            progressBar,
            "progress",
            0,
            value
        ).setDuration(1000).start()
    }
}