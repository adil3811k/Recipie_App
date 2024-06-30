package com.example.recipieapp

import android.app.Application
import com.example.recipieapp.data.AppContainer
import com.example.recipieapp.data.DefaultAppContainer

class RecipeApplication  :Application(){
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}