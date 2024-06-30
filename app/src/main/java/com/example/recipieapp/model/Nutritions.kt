package com.example.recipieapp.model

data class Nutritions(
    val bad: List<bad>,
    val caloricBreakdown: CaloricBreakdown,
    val calories: String,
    val carbs: String,
    val expires: Long,
    val fat: String,
    val flavonoids: List<Flavonoid>,
    val good: List<Good>,
    val ingredients: List<IngredientX>,
    val isStale: Boolean,
    val nutrients: List<NutrientX>,
    val properties: List<Property>,
    val protein: String,
    val weightPerServing: WeightPerServing
)