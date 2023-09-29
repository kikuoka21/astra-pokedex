package com.example.astrapokedex.modal

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.astrapokedex.R
import com.squareup.picasso.Picasso
import java.util.*

@BindingAdapter("imageUrl")
fun bindImage(view: ImageView, linkFoto: String?) {
    linkFoto?.let {

        val picassoBuilder = Picasso.Builder(view.context)
        val picasso = picassoBuilder.build()
        picasso
            .load(linkFoto)
            .error(R.drawable.ic_baseline_hide)
            .placeholder(R.drawable.ic_baseline)
            .into(view)

    }
}

@BindingAdapter("textBeratTinggi")
fun textBeratTinggi(view: TextView, satuan: Int) {


    val angkanya = satuan / 10.0
    val result = if (angkanya % 1 == 0.0) angkanya.toInt() else angkanya

    view.text = "$result"
}
@BindingAdapter("longToText")
fun textLong(view: TextView, satuan: Long) {



    view.text = "$satuan"
}





