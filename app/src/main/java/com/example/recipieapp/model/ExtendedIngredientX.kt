package com.example.recipieapp.model

data class ExtendedIngredientX(
    val aisle: String,
    val amount: Double,
    val consistency: String,
    val id: Int,
    val image: String,
    val measures: MeasuresX,
    val meta: List<String>,
    val name: String,
    val nameClean: String,
    val original: String,
    val originalName: String,
    val unit: String
)