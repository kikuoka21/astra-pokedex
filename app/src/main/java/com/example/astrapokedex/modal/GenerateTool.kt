package com.example.astrapokedex.modal

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.Gravity
import android.view.Window
import android.widget.Toast
import com.example.astrapokedex.R
import javax.inject.Inject

class GenerateTool @Inject constructor(private val context: Context) {
    private var toast: Toast? = null

    fun initialDialog(context: Context): Dialog {
        val mDialog = Dialog(context)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setContentView(R.layout.custom_progres_bar)
        mDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return mDialog
    }

    fun isInternetAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }


    fun popUpMessage(context: Context, tittle: String, message: String) {
        val alert = AlertDialog.Builder(context).create()
        alert.setTitle(tittle)
        alert.setMessage(message)
        alert.setCancelable(false)
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK") { dialog, _ ->
            dialog.dismiss()
        }
        alert.show()
    }

    fun popUpMessageFinish(context: Context, tittle: String, message: String) {
        val builder = AlertDialog.Builder(context).create()
        builder.setTitle(tittle)
        builder.setMessage(message)
        builder.setButton(AlertDialog.BUTTON_NEUTRAL, "OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.setOnDismissListener {
            (context as Activity).finish()
        }
        builder.show()


    }

    fun showToast(text: String) {

        toast?.cancel()
        toast = Toast.makeText(context, "message", Toast.LENGTH_SHORT)
        toast?.setText(text)
        toast?.setGravity(Gravity.BOTTOM, 0, 50)
        toast?.show()
    }
}