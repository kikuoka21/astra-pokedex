package com.example.astrapokedex.screen.homePage

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.astrapokedex.MyApplication
import com.example.astrapokedex.R
import com.example.astrapokedex.databinding.HomePageBinding
import com.example.astrapokedex.modal.DatabaseHelper
import com.example.astrapokedex.modal.GenerateTool
import com.example.astrapokedex.modal.model.Pokemon
import com.example.astrapokedex.screen.detilItem.DetilPokemon
import javax.inject.Inject

class HomePage : AppCompatActivity(), AdapterPokemon.BtnListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: Dialog
    private lateinit var pokemonAdapter: AdapterPokemon
    private var maxPokemon = 0

    private var arrayPokemon = ArrayList<Pokemon>()

    private val viewModel: HomePageViewModel by viewModels {
        VMFactoryHomePage(application)
    }

    private lateinit var binding: HomePageBinding

    @Inject
    lateinit var generateTool: GenerateTool

    @Inject
    lateinit var databaseHelper: DatabaseHelper


    private lateinit var option1MenuItem: MenuItem
    private lateinit var option2MenuItem: MenuItem
    private var shortingBy = DatabaseHelper.SearcAsc
    private var keyword = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).appComponent.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.home_page)
        binding.lifecycleOwner = this

        progressBar = generateTool.initialDialog(this)

        recyclerView = binding.recyclePokemon
        pokemonAdapter = AdapterPokemon(this, arrayPokemon)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = pokemonAdapter


        viewModel.showMessage.observe(this) { pesan ->

            if (pesan.show) {
                viewModel.setShowMessage(false)
                if (pesan.isFinish) generateTool.popUpMessageFinish(
                    this, pesan.tittle, pesan.message
                )
                else generateTool.popUpMessage(this, pesan.tittle, pesan.message)
            }
        }
        maxPokemon = databaseHelper.getPokemonCount()

        viewModel.pokemon.observe(this) { list ->
            arrayPokemon.clear()

            arrayPokemon.addAll(list)
            pokemonAdapter.notifyDataSetChanged()
            setKeterangan()

        }

        viewModel.getDataFromSQLITE(keyword, shortingBy)

        binding.statusSearch.setOnClickListener {
            val size = databaseHelper.getPokemonCount()
            generateTool.showToast("size pokemon $size $shortingBy")

        }

        binding.searchPokemon.addTextChangedListener(textWatcher)


    }


    private val handler = Handler(Looper.getMainLooper())
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                keyword = p0.toString().trim()

                viewModel.getDataFromSQLITE(keyword, shortingBy)
            }, 1000)
        }

        override fun afterTextChanged(p0: Editable?) {
        }

    }

    private fun setKeterangan() {
        val showingText = if (arrayPokemon.size == maxPokemon) resources.getString(
            R.string.showingEntries, maxPokemon
        )
        else resources.getString(
            R.string.showingEntriesOf, arrayPokemon.size, maxPokemon
        )
        binding.statusSearch.text = showingText

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main_page, menu)

        option1MenuItem = menu.findItem(R.id.option1)
        option2MenuItem = menu.findItem(R.id.option2)

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.option1 -> {
                // Aksi yang ingin Anda lakukan ketika Option 1 dipilih
                option1MenuItem.isChecked = true
                option2MenuItem.isChecked = false
                if (shortingBy != DatabaseHelper.SearcAsc) {

                    shortingBy = DatabaseHelper.SearcAsc


                    viewModel.getDataFromSQLITE(keyword, shortingBy)
                }
                true
            }
            R.id.option2 -> {
                // Aksi yang ingin Anda lakukan ketika Option 2 dipilih
                option1MenuItem.isChecked = false
                option2MenuItem.isChecked = true


                if (shortingBy != DatabaseHelper.SearcDesc) {

                    shortingBy = DatabaseHelper.SearcDesc

                    viewModel.getDataFromSQLITE(keyword, shortingBy)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun pindahActivity(pokemon: Pokemon) {
        val intent = Intent(this, DetilPokemon::class.java)
        intent.putExtra(DetilPokemon.extraUrl, pokemon.url)
        startActivity(intent)
    }

}