package com.example.pokedex.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pokedex.fragments.FragmentAbout
import com.example.pokedex.fragments.FragmentBaseStats
import com.example.pokedex.fragments.FragmentEvolutions

class PokemonPagerAdapter(fragmentActivity: FragmentActivity, private val pokemonId: Int) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment = FragmentAbout()
                val bundle = Bundle()
                bundle.putInt("POKE_ID", pokemonId)
                fragment.arguments = bundle
                return fragment
            }

            1 -> {
                val fragment = FragmentBaseStats()
                val bundle = Bundle()
                bundle.putInt("POKE_ID", pokemonId)
                fragment.arguments = bundle
                return fragment
            }

            2 -> {
                val fragment = FragmentEvolutions()
                val bundle = Bundle()
                bundle.putInt("POKE_ID", pokemonId)
                fragment.arguments = bundle
                return fragment
            }

            else -> FragmentAbout()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }


}