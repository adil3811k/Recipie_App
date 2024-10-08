package com.example.recipieapp.newtwork

import com.example.recipieapp.model.BulkFavoriteModulItem
import com.example.recipieapp.model.Equipments
import com.example.recipieapp.model.Nutritions
import com.example.recipieapp.model.Recipes
import com.example.recipieapp.model.detailRecipe
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//ebc485795f884985bb398307e7e5843e
//82c837f559f64ef7bbbc0e89051651ef

private val KEY = "ebc485795f884985bb398307e7e5843e"

interface ApiService{

    @GET("/recipes/random")
    suspend fun getRandomRecipe(
        @Query("apiKey") apiKey:String = KEY,
        @Query("number") number:Int = 100
    ):Recipes

    @GET("/recipes/{id}/information")
    suspend fun getDetail(
        @Path("id") id :Int,
        @Query("apiKey") apiKey: String = KEY
    ):detailRecipe

    @GET("/recipes/{id}/equipmentWidget.json")
    suspend fun getEquipments(
        @Path("id") id:Int,
        @Query("apiKey") apiKey: String = KEY
    ):Equipments

    @GET("/recipes/{id}/nutritionWidget.json")
    suspend fun getNutritious(
        @Path("id") id:Int,
        @Query("apiKey") apiKey: String = KEY
    ):Nutritions

    @GET("/recipes/autocomplete")
    suspend fun getAutoComplete(
        @Query("query") query:String,
        @Query("apiKey") apiKey: String = KEY
    ):List<Suggetion>
    @GET("/recipes/informationBulk")
    suspend fun getBulkRecipe(
        @Query("ids") ids:String,
        @Query("includeNutrition") includeNutrition:Boolean = false,
        @Query("apiKey") apiKey:String = KEY
    ):List<BulkFavoriteModulItem>
}