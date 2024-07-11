package com.example.recipieapp.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

enum class Screens{
    HomeScreen,
    Favorite_Recipes,
    RecipeView,
    Search,
}
//1002050 demo Recipe ID
@Composable
fun mainApp(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(navController =navController , startDestination = Screens.HomeScreen.toString()) {
        composable(route = Screens.HomeScreen.toString()){
            HomeScreen(navController = navController)
        }
        composable(route = Screens.RecipeView.toString()+"/{id}", arguments = listOf(navArgument("id"){type = NavType.StringType})){
            RecipeView(id = it.arguments?.getString("id").toString())
        }
        composable(route = Screens.Search.toString()){
            SearchScreeen(navHostController = navController)
        }
        composable("Demo"){
            RecipeView(id = "1002050")
        }
    }
}