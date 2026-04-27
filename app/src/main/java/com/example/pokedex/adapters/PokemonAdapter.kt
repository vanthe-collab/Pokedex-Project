package com.example.pokedex.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.Model.DataPokemon
import com.example.pokedex.activities.DetailPokemonActivity
import com.example.pokedex.databinding.ItemPokemonDapterBinding

class PokemonAdapter(private val list: List<DataPokemon>) :
    RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    private lateinit var types: List<String>
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PokemonAdapter.ViewHolder {
        val binding =
            ItemPokemonDapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonAdapter.ViewHolder, position: Int) {
        val viTri = list[position]
        holder.binding.pokemonCardView.setDataPokemon(
            viTri.id,
            viTri.name,
            viTri.types,
            viTri.imageUrl
        )
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailPokemonActivity::class.java)
            intent.putExtra("POKE_DATA", viTri)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: ItemPokemonDapterBinding) :
        RecyclerView.ViewHolder(binding.root)


}