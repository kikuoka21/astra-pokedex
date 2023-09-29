package com.example.astrapokedex.screen.splashScreen

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
import com.example.astrapokedex.modal.DatabaseHelper
import com.example.astrapokedex.modal.GenerateTool
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class ViewModelSplashScreen(application: Application) : AndroidViewModel(application) {


    private var apiURL = ""

    private val _volleyRun = MutableLiveData<Boolean>()
    val volleyRun: LiveData<Boolean>
        get() = _volleyRun

    @Inject
    lateinit var databaseHelper: DatabaseHelper

    @Inject
    lateinit var generateTool: GenerateTool



    init {
        (getApplication() as MyApplication).appComponent.inject(this)
        val pageSize = 100
        apiURL = "https://pokeapi.co/api/v2/pokemon/?limit=$pageSize"

        _volleyRun.value = true
    }


    fun runVolleyPokemon() {

        Timber.e("APi = $apiURL")
        val stringRequest = object : StringRequest(Method.GET, apiURL, { response: String ->


            val jsonObject = JSONObject(response)

            val jsonArray = jsonObject.getJSONArray("results")
            for (i in 0 until jsonArray.length()) {

                val jsonObjectPokemon = jsonArray.getJSONObject(i)

                databaseHelper.insertPokemon(
                    jsonObjectPokemon.getString("name"), jsonObjectPokemon.getString("url")
                )

            }

            Timber.e(apiURL)
            if (!jsonObject.isNull("next")) {
                apiURL = jsonObject.getString("next")
                runVolleyPokemon()
            } else {

                _volleyRun.value = false
            }


            //
        }, Response.ErrorListener {
            _volleyRun.value = false


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