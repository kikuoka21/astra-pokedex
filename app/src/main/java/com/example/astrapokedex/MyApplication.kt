package com.example.astrapokedex

import android.app.Application
import com.example.astrapokedex.modal.AppComponent
import com.example.astrapokedex.modal.AppModule
import com.example.astrapokedex.modal.DaggerAppComponent
import timber.log.Timber

class MyApplication : Application(){
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()

//        appComponent.inject(this)

        Timber.plant(Timber.DebugTree())

        Timber.d("================================INITIAL timber===================================")
    }
}