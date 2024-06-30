package com.example.recipieapp.newtwork

import com.example.recipieapp.model.Equipments
import com.example.recipieapp.model.Nutritions
import com.example.recipieapp.model.Recipes
import com.example.recipieapp.model.detailRecipe
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService{

    @GET("/recipes/random")
    suspend fun getRandomRecipe(
        @Query("apiKey") apiKey:String = "ebc485795f884985bb398307e7e5843e",
        @Query("number") number:Int = 100
    ):Recipes

    @GET("/recipes/{id}/information")
    suspend fun getDetail(
        @Path("id") id :Int,
        @Query("apiKey") apiKey: String = "ebc485795f884985bb398307e7e5843e"
    ):detailRecipe

    @GET("/recipes/{id}/equipmentWidget.json")
    suspend fun getEquipments(
        @Path("id") id:Int,
        @Query("apiKey") apiKey: String = "ebc485795f884985bb398307e7e5843e"
    ):Equipments

    @GET("/recipes/{id}/nutritionWidget.json")
    suspend fun getNutritious(
        @Path("id") id:Int,
        @Query("apiKey") apiKey: String = "ebc485795f884985bb398307e7e5843e"
    ):Nutritions
}