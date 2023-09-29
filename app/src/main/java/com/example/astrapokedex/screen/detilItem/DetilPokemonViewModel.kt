package com.example.astrapokedex.screen.detilItem

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkError
import com.android.volley.Response
import com.android.volley.TimeoutError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.astrapokedex.MyApplication
import com.example.astrapokedex.modal.GenerateTool
import com.example.astrapokedex.modal.model.ModelDetilPokemon
import com.example.astrapokedex.modal.model.ModelPesan
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class DetilPokemonViewModel(application: Application) : AndroidViewModel(application) {

    private var _pokemon = MutableLiveData<ModelDetilPokemon>()
    val pokemon: LiveData<ModelDetilPokemon>
        get() = _pokemon

    private val _volleyRun = MutableLiveData<Boolean>()
    val volleyRun: LiveData<Boolean>
        get() = _volleyRun

    private val _showMessage = MutableLiveData<ModelPesan>()
    val showMessage: LiveData<ModelPesan>
        get() = _showMessage


    @Inject
    lateinit var generateTool: GenerateTool

    init {
        (getApplication() as MyApplication).appComponent.inject(this)
        setShowMessage(false)
        _volleyRun.value = false
    }

    fun setShowMessage(
        value: Boolean,
        tittle: String = "Informasi",
        message: String = "Message is Empty",
        finish: Boolean = true
    ) {
        _showMessage.value =
            ModelPesan(show = value, tittle = tittle, message = message, isFinish = finish)
    }


    fun runVolleyPokemon(apiURL: String) {
        if (generateTool.isInternetAvailable())
            volley(apiURL)
        else {
            setShowMessage(
                value = true, message = "Anda tidak terhubung dengan jaringan", finish = true
            )
        }

    }

    private fun volley(apiURL: String) {
        if (_volleyRun.value == true) return

        _volleyRun.value = true
        Timber.e("APi = $apiURL")
        val stringRequest = object : StringRequest(Method.GET, apiURL, { response: String ->
            _volleyRun.value = false
            try {

                val jsonObject = JSONObject(response)
                val finalHeight = jsonObject.getInt("height")
                val finalWeight = jsonObject.getInt("weight")

                Timber.e("$finalHeight  $finalWeight")
                val abilitiesJson = jsonObject.getJSONArray("abilities")
                val listAbilities = ArrayList<String>()
                for (i in 0 until abilitiesJson.length()) {
                    var jsontemp = abilitiesJson.getJSONObject(i)
                    jsontemp = jsontemp.getJSONObject("ability")
                    listAbilities.add(jsontemp.getString("name"))
                    Timber.e(jsontemp.getString("name"))
                }

                Timber.e("===============")
                val typejson = jsonObject.getJSONArray("types")
                val listTypes = ArrayList<String>()
                for (i in 0 until typejson.length()) {
                    var jsontemp = typejson.getJSONObject(i)
                    jsontemp = jsontemp.getJSONObject("type")
                    listTypes.add(jsontemp.getString("name"))
                    Timber.e(jsontemp.getString("name"))
                }

                val jsontemp = jsonObject.getJSONObject("sprites")
                val datanya = ModelDetilPokemon(
                    jsonObject.getString("name"),
                    jsontemp.getString("front_default"),
                    listAbilities,
                    listTypes,
                    "$finalWeight",
                    "$finalHeight"
                )
                _pokemon.value = datanya
            } catch (e: java.lang.Exception) {
                e.message?.let { message ->
                    setShowMessage(
                        value = true, message = message, finish = true
                    )
                }

            }

            //
        }, Response.ErrorListener { error ->
            _volleyRun.value = false

            if (!(error is NetworkError || error is TimeoutError)) {

                val networkResponse = error.networkResponse
                try {
                    val jsonObject = JSONObject(String(networkResponse.data))
                    Timber.e(jsonObject.toString(3))
                    setShowMessage(
                        value = true,
                        tittle = jsonObject.getString("code"),
                        message = jsonObject.getString("message"),
                        finish = true
                    )
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    setShowMessage(
                        value = true,
                        message = "Unexpected response code " + networkResponse.statusCode.toString(),
                        finish = true
                    )
                }

            } else {
                setShowMessage(
                    value = true,
                    message = "Gagal terhubung dengan server, silahkan coba beberapa saat lagi.",
                    finish = true
                )

            }
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = mutableMapOf<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
//                headers["X-Api-Key"] = generateTool.token
                Timber.e(headers.toString())
                return headers
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
            20000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        Volley.newRequestQueue(getApplication()).add(stringRequest)
    }
}