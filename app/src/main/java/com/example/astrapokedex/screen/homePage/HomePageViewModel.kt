package com.example.astrapokedex.screen.homePage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.astrapokedex.MyApplication
import com.example.astrapokedex.modal.DatabaseHelper
import com.example.astrapokedex.modal.model.ModelPesan
import com.example.astrapokedex.modal.model.Pokemon
import timber.log.Timber
import java.sql.Time
import javax.inject.Inject

class HomePageViewModel(application: Application) : AndroidViewModel(application) {

    private var apiURL = ""
    private var _pokemon = MutableLiveData<List<Pokemon>>()
    val pokemon: LiveData<List<Pokemon>>
        get() = _pokemon

    private val _showMessage = MutableLiveData<ModelPesan>()
    val showMessage: LiveData<ModelPesan>
        get() = _showMessage


    @Inject
    lateinit var databaseHelper: DatabaseHelper

    fun setShowMessage(
        value: Boolean,
        tittle: String = "Informasi",
        message: String = "Message is Empty",
        finish: Boolean = true
    ) {
        _showMessage.value =
            ModelPesan(show = value, tittle = tittle, message = message, isFinish = finish)
    }


    init {
        (getApplication() as MyApplication).appComponent.inject(this)
//        val pageSize = 10
        val pageSize = 30
        apiURL = "https://pokeapi.co/api/v2/pokemon/?limit=$pageSize"
    }


    fun getDataFromSQLITE(kataKunci: String = "", shorting: String) {
        val listPokemonDB = databaseHelper.searchPokemonByName(kataKunci, shorting)
        _pokemon.value = listPokemonDB
    }
}