package com.example.pokedex.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokedex.Model.EvolutionItem
import com.example.pokedex.databinding.ItemEvolutionBinding
import com.example.pokedex.fragments.FragmentEvolutions

class PokemonEvolutionChainAdapter(private val list: List<EvolutionItem>) :
    RecyclerView.Adapter<PokemonEvolutionChainAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemEvolutionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val viTri = list[position]
        holder.binding.tvEvoName.text = viTri.name
        Glide.with(holder.itemView.context).load(viTri.imageUrl).into(holder.binding.ivEvoPokemon)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: ItemEvolutionBinding) :
        RecyclerView.ViewHolder(binding.root)

}