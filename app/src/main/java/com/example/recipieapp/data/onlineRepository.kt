package com.example.recipieapp.data

import com.example.recipieapp.model.Equipments
import com.example.recipieapp.model.Nutritions
import com.example.recipieapp.model.Recipes
import com.example.recipieapp.model.detailRecipe
import com.example.recipieapp.newtwork.ApiService
import com.example.recipieapp.newtwork.Suggetion
import retrofit2.http.Query

interface RepositoryRecipeApp{
    suspend fun gerRandomRecipes():Recipes
    suspend fun getSuggestions(qurey: String):List<Suggetion>
    suspend fun getDetailRecipe(id:Int):detailRecipe
    suspend fun getEquipments(id:Int):Equipments
    suspend fun getNutrition(id: Int):Nutritions
}
class NetworkRepositoryRecipeApp(private val apiService: ApiService):RepositoryRecipeApp{
    override suspend fun gerRandomRecipes(): Recipes =  apiService.getRandomRecipe()
    override suspend fun getSuggestions(qurey:String): List<Suggetion>  = apiService.getAutoComplete(qurey)
    override suspend fun getDetailRecipe(id: Int): detailRecipe =apiService.getDetail(id)
    override suspend fun getEquipments(id: Int): Equipments = apiService.getEquipments(id)
    override suspend fun getNutrition(id: Int): Nutritions = apiService.getNutritious(id)
}