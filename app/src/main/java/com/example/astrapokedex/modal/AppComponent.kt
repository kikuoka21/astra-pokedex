package com.example.astrapokedex.modal

import com.example.astrapokedex.MyApplication
import com.example.astrapokedex.screen.detilItem.DetilPokemon
import com.example.astrapokedex.screen.detilItem.DetilPokemonViewModel
import com.example.astrapokedex.screen.homePage.HomePage
import com.example.astrapokedex.screen.homePage.HomePageViewModel
import com.example.astrapokedex.screen.splashScreen.HalamanAwal
import com.example.astrapokedex.screen.splashScreen.ViewModelSplashScreen
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(application: MyApplication)

    fun inject(activity: HalamanAwal)
    fun inject(activity: ViewModelSplashScreen)

    fun inject(activity: HomePage)
    fun inject(activity: HomePageViewModel)

    fun inject(activity: DetilPokemonViewModel)
    fun inject(activity: DetilPokemon)
}