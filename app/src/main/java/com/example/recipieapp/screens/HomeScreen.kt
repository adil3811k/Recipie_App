@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.recipieapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.recipieapp.R
import com.example.recipieapp.model.Recipe
import com.example.recipieapp.ui.theme.RecipieAppTheme
import com.example.recipieapp.viewmodel.MainScreenViewmodel
import com.example.recipieapp.viewmodel.listOfRecipiseUIState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

data class BottomNavigationItems(
    val tital:String,
     val Icon:Int
)

val items = listOf(
    BottomNavigationItems(
        "Homr",
        R.drawable.vector,
    ),
    BottomNavigationItems(
        "Favorite",
        R.drawable.heart
        )
)

@Composable
fun HomeScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val viewmodel:MainScreenViewmodel  = viewModel(factory = MainScreenViewmodel.factory)
    var selectedScreenIndex by remember{ mutableStateOf(0) }
    Scaffold (
        topBar ={if (selectedScreenIndex==0) RecipeTopAppBar() },
        bottomBar ={
            BottomAppBar(actions =  {
                Row (
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ){
                    IconButton(onClick = { selectedScreenIndex = 0 }) {
                        Icon(
                            painter = painterResource(if (selectedScreenIndex==0) R.drawable.vector else R.drawable.house),
                            contentDescription = "",
                            tint = Color.Unspecified
                        )
                    }
                    IconButton(onClick = { selectedScreenIndex = 1 }) {
                        Icon(
                            painter = painterResource(if(selectedScreenIndex!=1)R.drawable.heart else R.drawable.vector__1_),
                            contentDescription = "",
                            tint = Color.Unspecified
                        )
                    }
                }
            })
        },
    ){innperpadding->
        if (selectedScreenIndex==0){
            HomeSp(
                viewmodel = viewmodel,
                parentNavyController = navController,
                modifier = Modifier.padding(innperpadding)
            )
        }else{
            FavoriteSp()
        }
    }
}



@Composable
fun HomeSp(
    viewmodel: MainScreenViewmodel,
    parentNavyController:NavHostController,
    modifier: Modifier = Modifier
) {
    val uiState = viewmodel.uiState.collectAsState()

        Column (
            modifier= modifier
                .padding(start = 20.dp)
        ){
            TextField(
                value = "",
                label = { Text(text = "Search any recipe")},
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "")},
                onValueChange = {},
                readOnly = true,
                colors = TextFieldDefaults.colors(unfocusedContainerColor = MaterialTheme.colorScheme.secondary, unfocusedLabelColor = Color.Black),
                modifier = Modifier
                    .padding(end = 20.dp)
                    .fillMaxWidth()
                    .height(53.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { parentNavyController.navigate(Screens.Search.toString()) },
                enabled = false
            )
            Text(
                text = "Discover tasty and healthy receipt",
                fontSize = 16.sp
            )
            bodyCompose(
                listOfRecipiseUIState = uiState.value,
                onitemClick = { it->parentNavyController.navigate(Screens.RecipeView.toString()+"/$it")}
            )
        }
    }

@Composable
fun bodyCompose(
    listOfRecipiseUIState: listOfRecipiseUIState,
    modifier: Modifier=Modifier,
    onitemClick: (Int) -> Unit
) {
    when(listOfRecipiseUIState){
        is listOfRecipiseUIState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(1f), contentAlignment = Alignment.Center){
                CircularProgressIndicator()
            }
        }
        is listOfRecipiseUIState.Sucsess -> {
            val propuler = mutableListOf<Recipe>()
            listOfRecipiseUIState.list.forEach {
                if (it.veryPopular){
                    propuler.add(it)
                }
            }
            Column {
                Text(
                    text = "Popular Recipes",
                    fontWeight = FontWeight(500),
                    fontSize = 21.sp,
                )
                LazyRow {
                    items(propuler){
                        populercompose(recipe = it, modifier = modifier.clickable { onitemClick(it.id) })
                    }
                }
                Spacer(modifier = modifier.height(20.dp))
                Text(
                    text = "All recipes",
                    fontWeight = FontWeight(700),
                    fontSize = 24.sp
                )
                LazyColumn {
                    items(listOfRecipiseUIState.list){
                        allRecipes(recipe = it,modifier= modifier.clickable { onitemClick(it.id) })
                    }
                }
            }
        }

        is listOfRecipiseUIState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                Text(text = listOfRecipiseUIState.massage)
            }
        }
    }
}
@Composable
fun populercompose(
    recipe: Recipe,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(180.dp)
            .clip(RoundedCornerShape(12.dp))
            .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
    ){
        AsyncImage(
            model = recipe.image,
            contentDescription = "",
            placeholder = painterResource(id = R.drawable.loading),
            contentScale = ContentScale.Crop,
            modifier = modifier
                .clip(RoundedCornerShape(12.dp))
                .fillMaxSize()
        )
        Box(modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black
                    ),
                    startY = 1f
                )
            ))
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ){
            Text(
                text = recipe.title,
                color = Color.White,
                fontSize = 16.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight(600),
                maxLines = 1,
                modifier = modifier.padding(start = 12.dp)
            )
            Text(
                text = "Ready in ${recipe.readyInMinutes} min",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight(400),
                lineHeight = 18.sp,
                maxLines = 1,
                modifier =modifier.padding(10.dp)
            )
        }
    }
}


@ExperimentalMaterial3Api
@Composable
fun RecipeTopAppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = { Text(text = "\uD83D\uDC4B Hey ${Firebase.auth.currentUser?.displayName}", fontWeight = FontWeight(500))}
    )
}



@Composable
fun FavoriteSp(modifier: Modifier = Modifier) {
    Text(text = "Favorite")
}




@Composable
fun allRecipes(
    recipe: Recipe,
    modifier: Modifier = Modifier
) {
    Row (
        modifier= modifier
            .padding(start = 10.dp, end = 20.dp, bottom = 20.dp)
            .fillMaxWidth()
            .border(2.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
    ){
        AsyncImage(
            model = recipe.image,
            contentDescription = recipe.summary,
            placeholder = painterResource(id = R.drawable.loading),
            contentScale = ContentScale.Crop,
            modifier = modifier.size(100.dp)
        )
        Column (
            modifier = modifier
                .padding(start = 10.dp)
                .weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ){
            Text(text = recipe.title)
            Text(text = "Ready in ${recipe.readyInMinutes} min")
        }
    }
}
