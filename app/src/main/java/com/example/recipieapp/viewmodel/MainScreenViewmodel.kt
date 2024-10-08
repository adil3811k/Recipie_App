package com.example.recipieapp.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.recipieapp.RecipeApplication
import com.example.recipieapp.data.NetworkRepositoryRecipeApp
import com.example.recipieapp.model.Equipments
import com.example.recipieapp.model.Nutritions
import com.example.recipieapp.model.Recipe
import com.example.recipieapp.model.detailRecipe
import com.example.recipieapp.newtwork.FirebaseService
import com.example.recipieapp.newtwork.Suggetion
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

private val TAG = "FirebaseFireStore"
sealed interface listOfRecipiseUIState{
    object Loading:listOfRecipiseUIState
    data class Sucsess(val list: List<Recipe>):listOfRecipiseUIState
    data class Error(val massage:String):listOfRecipiseUIState
}

sealed interface DetailRecipeUIState{
    object Loading:DetailRecipeUIState
    data class Error(val message:String):DetailRecipeUIState
    data class Sucsess(
        val detail:detailRecipe,
        val equipments: Equipments,
        val nutritions: Nutritions,
    ):DetailRecipeUIState
}

sealed interface FavoritesUiState{
    object Loading:FavoritesUiState
    data class Succeed(val isFavorite: Boolean):FavoritesUiState
    data class Error(val message: String):FavoritesUiState
}

sealed interface SuggetionUIState{
    object Ideal:SuggetionUIState
    data class Error(val message: String):SuggetionUIState
    data class Sucsess(val suggetions:List<Suggetion>):SuggetionUIState
    object Loading:SuggetionUIState
}
class MainScreenViewmodel(
    private val networkRepositoryRecipeApp: NetworkRepositoryRecipeApp
) :ViewModel(){
    private var _listOfRecipiseUIState : MutableStateFlow<listOfRecipiseUIState> = MutableStateFlow(listOfRecipiseUIState.Loading)
    val uiState = _listOfRecipiseUIState.asStateFlow()
    private var _detailRecipUIState:MutableStateFlow<DetailRecipeUIState> = MutableStateFlow(DetailRecipeUIState.Loading)
    val detailRecipeUIState = _detailRecipUIState.asStateFlow()
    private var _suggetionUIState = MutableStateFlow<SuggetionUIState>(SuggetionUIState.Ideal)
    val suggetionUIState = _suggetionUIState.asStateFlow()
    private var  _FavoritesUiState= MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)
    val favoritesUiState = _FavoritesUiState.asStateFlow()

    private val firebaseService = FirebaseService()
    fun getRandomRecipes(){
        viewModelScope.launch {
            _listOfRecipiseUIState.update {
                try {
                    val data = networkRepositoryRecipeApp.gerRandomRecipes()
                    listOfRecipiseUIState.Sucsess(data.recipes)
                }catch (e:Exception){
                    listOfRecipiseUIState.Error(e.message.toString())
                }
            }
        }
    }

    fun getsuggetions(test:String){
        viewModelScope.launch {
            _suggetionUIState.update {
                try {
                    val list = networkRepositoryRecipeApp.getSuggestions(test)
                    SuggetionUIState.Sucsess(list)
                }catch (e:Exception){
                    SuggetionUIState.Error(e.message.toString())
                }
            }
        }
    }

    fun getDetail(id:Int){
        viewModelScope.launch {
            _detailRecipUIState.update {
                try {
                    withTimeout(10000){
                        val detail = networkRepositoryRecipeApp.getDetailRecipe(id)
                        val equipments = networkRepositoryRecipeApp.getEquipments(id)
                        val nutritions= networkRepositoryRecipeApp.getNutrition(id)
                        getFavorets(id.toString())
                        DetailRecipeUIState.Sucsess(detail,equipments,nutritions)
                    }
                }catch (e:Exception){
                    DetailRecipeUIState.Error(e.message.toString())
                }catch (e:TimeoutCancellationException){
                    DetailRecipeUIState.Error("Time out")
                }
            }
        }
    }
    private suspend  fun getFavorets(id: String){
        _FavoritesUiState.update {
            try {
                FavoritesUiState.Succeed(firebaseService.isFavorites(id))
            }catch (e:Exception){
                Log.d(TAG , e.message?:"Error")
                FavoritesUiState.Error(e.message?:"Error")
            }
        }
    }
    suspend fun isfavoterToagal(id: String){
            _FavoritesUiState.value =  FavoritesUiState.Loading
            try {
                firebaseService.TogalFavorets(id)
                getFavorets(id)
            }catch (e:Exception){
                Log.d(TAG , e.message?:"Error 2")
                _FavoritesUiState.value= FavoritesUiState.Error(e.message?:"Error")
            }
        }
    init {
        getRandomRecipes()
    }


    companion object {
        val factory  = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as RecipeApplication
                MainScreenViewmodel(application.container.networkRepositoryRecipeApp)
            }
        }
    }
}