package com.example.astrapokedex.screen.homePage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.astrapokedex.R
import com.example.astrapokedex.databinding.RowPokemonBinding
import com.example.astrapokedex.modal.model.Pokemon
import timber.log.Timber

class AdapterPokemon(
    private val jembatannya: BtnListener,
    private val arrayPokemon: ArrayList<Pokemon>
) : RecyclerView.Adapter<PokemonViewHolder>() {

    private lateinit var binding: RowPokemonBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        binding = RowPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = arrayPokemon[position]
//        var rescolor = R.color.bg_genap
//        if (position % 2 == 1) {
//            rescolor = R.color.bg_ganjil
//        }
        binding.itemRow.setCardBackgroundColor(
            ContextCompat.getColor(
                holder.itemView.context,
                if (position % 2 == 1) R.color.bg_genap else R.color.bg_ganjil
            )
        )
        Timber.e("${position % 2 == 1} ${pokemon.name}")
        holder.bind(pokemon, jembatannya)
    }

    override fun getItemCount(): Int {
        return arrayPokemon.size
    }

    interface BtnListener {
        fun pindahActivity(pokemon: Pokemon)
    }
}

class PokemonViewHolder(private val binding: RowPokemonBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(pokemon: Pokemon, itemClick: AdapterPokemon.BtnListener) {

        binding.pokemon = pokemon
        binding.itemRow.setOnClickListener {
            itemClick.pindahActivity(pokemon)
        }

    }
}