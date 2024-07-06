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
        @Query("apiKey") apiKey:String = "bfece9704b2348de9cb27c83ecff42ab",
        @Query("number") number:Int = 100
    ):Recipes

    @GET("/recipes/{id}/information")
    suspend fun getDetail(
        @Path("id") id :Int,
        @Query("apiKey") apiKey: String = "bfece9704b2348de9cb27c83ecff42ab"
    ):detailRecipe

    @GET("/recipes/{id}/equipmentWidget.json")
    suspend fun getEquipments(
        @Path("id") id:Int,
        @Query("apiKey") apiKey: String = "bfece9704b2348de9cb27c83ecff42ab"
    ):Equipments

    @GET("/recipes/{id}/nutritionWidget.json")
    suspend fun getNutritious(
        @Path("id") id:Int,
        @Query("apiKey") apiKey: String = "bfece9704b2348de9cb27c83ecff42ab"
    ):Nutritions

    @GET("/recipes/autocomplete")
    suspend fun getAutoComplete(
        @Query("query") query:String,
        @Query("apiKey") apiKey: String = "bfece9704b2348de9cb27c83ecff42ab"
    ):List<Suggetion>
}