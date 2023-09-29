package com.example.astrapokedex.screen.splashScreen

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import com.example.astrapokedex.MyApplication
import com.example.astrapokedex.R
import com.example.astrapokedex.modal.DatabaseHelper
import com.example.astrapokedex.modal.GenerateTool
import com.example.astrapokedex.screen.homePage.HomePage
import com.google.android.material.button.MaterialButton
import javax.inject.Inject

class HalamanAwal : AppCompatActivity() {
    private val viewModel: ViewModelSplashScreen by viewModels {
        VMFactorySplasScreen(application)
    }
    @Inject
    lateinit var generateTool: GenerateTool

    @Inject
    lateinit var databaseHelper: DatabaseHelper

    private lateinit var progressBar: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        (application as MyApplication).appComponent.inject(this)

        findViewById<MaterialButton>(R.id.start).setOnClickListener {

            startActivity(Intent(this, HomePage::class.java))
        }
        progressBar = generateTool.initialDialog(this)

        viewModel.volleyRun.observe(this) { show ->
            if (show)
                progressBar.show()
            else
                progressBar.dismiss()


        }

        viewModel.runVolleyPokemon()

        findViewById<TextView>(R.id.status).setOnClickListener {
            val size = databaseHelper.getPokemonCount()
            generateTool.showToast("size pokemon $size")
        }

        findViewById<ImageView>(R.id.imageHapus).setOnClickListener {
            databaseHelper.deleteAllPokemons()
            generateTool.showToast("sudah dihapus")
        }

    }
}