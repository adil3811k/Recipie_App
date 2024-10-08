package com.example.recipieapp.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipieapp.R

data class navigationHelperCalss(
    val tital :String,
    @DrawableRes val selectecIcons:Int,
    @DrawableRes val unSelectedIcon:Int,
)
val NavigationList = listOf(
    navigationHelperCalss(
        "Home",
        R.drawable.vector,
        R.drawable.house
    ),
    navigationHelperCalss(
        "Favorites",
        R.drawable.vector__1_,
        R.drawable.heart
    )
)
val paddingvalue = PaddingValues(
    start = 0.dp,
    top = 0.dp,
    end = 0.dp,
    bottom = 0.dp
)
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
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
    Scaffold(
        bottomBar = {
            BottomAppBar(contentPadding = paddingvalue) {
                NavigationList.forEachIndexed{Indext , item->
                    NavigationBarItem(
                        selected = selectedItemIndex==Indext,
                        onClick = {
                            selectedItemIndex = Indext
                            when(Indext){
                                0->navController.navigate(Screens.HomeScreen.toString())
                                1->navController.navigate(Screens.Favorite_Recipes.toString())
                            }
                        },
                        alwaysShowLabel = false,
                        label = { Text(item.tital) },
                        icon = {
                            Icon(
                                painter = painterResource(if (Indext==selectedItemIndex) item.selectecIcons else item.unSelectedIcon),
                                contentDescription = item.tital,
                                tint = Color.Unspecified
                            )
                        }
                    )
                }
            }
        }
    ) {innerpadding->
        NavHost(navController =navController , startDestination = Screens.HomeScreen.toString(),modifier = modifier.padding(innerpadding)) {
            composable(route = Screens.HomeScreen.toString()){
                HomeScreen(navController = navController)
            }
            composable(route = Screens.RecipeView.toString()+"/{id}", arguments = listOf(navArgument("id"){type = NavType.StringType})){
                RecipeView(id = it.arguments?.getString("id").toString())
            }
            composable(route = Screens.Search.toString()){
                SearchScreeen(navHostController = navController)
            }
            composable(route = Screens.Favorite_Recipes.toString()){
                FavoritesScreen(navController)
            }
            composable("Demo"){
                RecipeView(id = "1002050")
            }
        }
    }
}