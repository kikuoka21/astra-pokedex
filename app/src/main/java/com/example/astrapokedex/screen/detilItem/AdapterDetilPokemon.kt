package com.example.astrapokedex.screen.detilItem

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.astrapokedex.databinding.RowDetilBinding

class AdapterDetilPokemon(private val itemList: List<String>) : RecyclerView.Adapter<ViewHolder>() {

    private lateinit var binding: RowDetilBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = RowDetilBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}

class ViewHolder(private val binding: RowDetilBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(text: String) {

        binding.textnya = text


    }
}
