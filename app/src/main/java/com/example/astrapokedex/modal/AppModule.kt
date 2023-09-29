package com.example.astrapokedex.modal

import com.example.astrapokedex.MyApplication
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: MyApplication) {
    @Provides
    fun provideGenerateTool(): GenerateTool {
        return GenerateTool(application)
    }

    @Provides
    fun provideDatabaseHelper(): DatabaseHelper {
        return DatabaseHelper(application)
    }
}