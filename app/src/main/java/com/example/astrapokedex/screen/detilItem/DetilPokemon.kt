package com.example.astrapokedex.screen.detilItem

import android.app.Dialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.Carousel.Adapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.astrapokedex.MyApplication
import com.example.astrapokedex.R
import com.example.astrapokedex.databinding.DetilMainBinding
import com.example.astrapokedex.modal.GenerateTool
import javax.inject.Inject

class DetilPokemon : AppCompatActivity() {
    companion object{
        const val extraUrl = "ExtraUrl"
    }
    private lateinit var binding: DetilMainBinding

    @Inject
    lateinit var generateTool: GenerateTool

    private lateinit var progressBar: Dialog
    private lateinit var adapterAbilities: AdapterDetilPokemon
    private lateinit var adapterType: AdapterDetilPokemon

    private lateinit var recycleAbilities: RecyclerView
    private lateinit var recycleType: RecyclerView

    private val viewModel: DetilPokemonViewModel by viewModels {
        VMFactoryDetilPokemon(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).appComponent.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.detil_main)
        binding.lifecycleOwner = this

        progressBar = generateTool.initialDialog(this)

        recycleAbilities = binding.recycleAbilities
        recycleAbilities.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recycleType = binding.recycleType
        recycleType.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        viewModel.showMessage.observe(this) { pesan ->
            if (pesan.show) {
                viewModel.setShowMessage(false)
                if (pesan.isFinish)
                    generateTool.popUpMessageFinish(this, pesan.tittle, pesan.message)
                else
                    generateTool.popUpMessage(this, pesan.tittle, pesan.message)
            }
        }

        viewModel.volleyRun.observe(this) { show ->
            if (show)
                progressBar.show()
            else
                progressBar.dismiss()

        }

        viewModel.pokemon.observe(this){modelnya->
            binding.pokemon =modelnya

            adapterAbilities = AdapterDetilPokemon(modelnya.abilities)
            recycleAbilities.adapter = adapterAbilities

            adapterType = AdapterDetilPokemon(modelnya.type)
            recycleType.adapter = adapterType

        }

        viewModel.runVolleyPokemon(intent.getStringExtra(extraUrl).toString())
    }
}