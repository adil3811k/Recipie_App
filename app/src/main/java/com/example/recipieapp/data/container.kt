package com.example.recipieapp.data

import androidx.compose.ui.layout.ScaleFactor
import com.example.recipieapp.newtwork.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer{
    val networkRepositoryRecipeApp:NetworkRepositoryRecipeApp
}

class DefaultAppContainer:AppContainer{
    private val BASEURL = "https://api.spoonacular.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val retrofitService by lazy {
        retrofit.create(ApiService::class.java)
    }
    override val networkRepositoryRecipeApp by lazy {
        NetworkRepositoryRecipeApp(retrofitService)
    }
}
