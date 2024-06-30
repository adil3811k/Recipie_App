package com.example.recipieapp.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

enum class Screens{
    Login_Singin,
    HomeScreen,
    Favorite_Recipes,
    RecipeView,
    Search,
}

@Composable
fun mainApp(

    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(navController =navController , startDestination = Screens.Login_Singin.toString()) {
        composable(route = Screens.Login_Singin.toString()){
            Login_SingUpScreen{
                navController.navigate(route = Screens.HomeScreen.toString())
            }
        }
        composable(route = Screens.HomeScreen.toString()){
            HomeScreen(navController = navController)
        }
        composable(route = Screens.RecipeView.toString()+"/{id}", arguments = listOf(navArgument("id"){type = NavType.StringType})){
            RecipeView(id = it.arguments?.getString("id").toString())
        }
        composable(route = Screens.Search.toString()){
            SearchScreeen()
        }
    }
}