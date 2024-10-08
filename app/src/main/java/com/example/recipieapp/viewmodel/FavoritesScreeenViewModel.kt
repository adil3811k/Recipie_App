package com.example.recipieapp.viewmodel

import android.provider.Contacts.Intents.UI
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.recipieapp.RecipeApplication
import com.example.recipieapp.data.NetworkRepositoryRecipeApp
import com.example.recipieapp.model.BulkFavoriteModulItem
import com.example.recipieapp.model.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private val UID = FirebaseAuth.getInstance().currentUser?.uid?:"Demo"
sealed interface FavoritesUIStat{
    object Loading : FavoritesUIStat
     data class Success(val list :List<BulkFavoriteModulItem>) : FavoritesUIStat
     data class Error(val message : String) : FavoritesUIStat
}

class FavoritesScreenViewModel(
    private val networkRepositoryRecipeApp: NetworkRepositoryRecipeApp
) : ViewModel(){
    private val _FavoriteList :MutableStateFlow<FavoritesUIStat> = MutableStateFlow(FavoritesUIStat.Loading)
    val favoritesUIStat = _FavoriteList.asStateFlow()

    private  fun sebcriptRealTime(){
        Firebase.firestore.collection(UID).document("Favorites")
            .addSnapshotListener { value, error ->
                viewModelScope.launch {
                    getLIst()
                }
            }
    }

    suspend fun getLIst(){
        _FavoriteList.value = FavoritesUIStat.Loading
        val Document =  Firebase.firestore.collection(UID).document("Favorites")
            .get().await()
        val list = Document.data!!.get("favorites") as? List<String>   ?: emptyList()
        var ids = list.joinToString(",")
        val result = networkRepositoryRecipeApp.getBulkRecipe(ids)
        _FavoriteList.value = FavoritesUIStat.Success(result)
    }

    init {
        viewModelScope.launch {
            sebcriptRealTime()
        }
    }

    companion object{
        val factory  = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as RecipeApplication
                FavoritesScreenViewModel(application.container.networkRepositoryRecipeApp)
            }
        }
    }
}