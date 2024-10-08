package com.example.recipieapp.model

data class ExtendedIngredientXX(
    val aisle: String,
    val amount: Double,
    val consistency: String,
    val id: Int,
    val image: String,
    val measures: MeasuresXX,
    val meta: List<String>,
    val name: String,
    val nameClean: String,
    val original: String,
    val originalName: String,
    val unit: String
)