package com.example.pokedex.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.Model.DataPokemon
import com.example.pokedex.CustomViews.PokemonCardView
import com.example.pokedex.databinding.ItemPokemonDapterBinding

class PokemonAdapter(private val list: List<DataPokemon>) :
    RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {
    private var delegateDetail: DelegateClickDeltail? = null
    private lateinit var types: List<String>
    fun setDelegateDetail(delegateClickDeltail: DelegateClickDeltail) {
        this.delegateDetail = delegateClickDeltail
    }


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
        holder.binding.pokemonCardView.setDelegate(object : PokemonCardView.DetailPokemon {
            override fun clickDetailPokemon(id: Int, name: String) {
                delegateDetail?.clickDetailPokemon(id, name)
            }

        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: ItemPokemonDapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface DelegateClickDeltail {
        fun clickDetailPokemon(id: Int, name: String)
    }
}