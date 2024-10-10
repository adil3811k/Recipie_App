package com.example.recipieapp.screens

import   Rout
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.recipieapp.R

private val TAG = "Rout"

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
//1002050 demo Recipe ID
@Composable
fun mainApp(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val backstackEntry by navController.currentBackStackEntryAsState()
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
    LaunchedEffect(backstackEntry?.destination?.route) {

    }
    Scaffold(
        bottomBar = {
            BottomAppBar(contentPadding = paddingvalue) {
                NavigationList.forEachIndexed{Indext , item->
                    NavigationBarItem(
                        selected = selectedItemIndex==Indext,
                        onClick = {
                            selectedItemIndex = Indext
                            when(Indext){
                                0->navController.navigate(Rout.Home)
                                1->navController.navigate(Rout.Favorites)
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
        NavHost(navController =navController , startDestination = Rout.Home,modifier = modifier.padding(innerpadding)) {
            composable<Rout.Home>{
                HomeScreen(navController = navController)
            }
            composable<Rout.RecipeView>{
                val rout:Rout.RecipeView = it.toRoute()
                RecipeView(id = rout.id)
            }
            composable<Rout.Searcher>{
                SearchScreeen(navHostController = navController)
            }
            composable<Rout.Favorites>{
                FavoritesScreen(navController)
            }
            composable<Rout.Demo>{
                RecipeView(id = "1002050")
            }
        }
    }
}