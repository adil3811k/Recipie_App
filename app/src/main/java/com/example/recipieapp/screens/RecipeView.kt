package com.example.recipieapp.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.recipieapp.R
import com.example.recipieapp.model.Good
import com.example.recipieapp.model.bad
import com.example.recipieapp.viewmodel.DetailRecipeUIState
import com.example.recipieapp.viewmodel.FavoritesUiState
import com.example.recipieapp.viewmodel.MainScreenViewmodel
import kotlinx.coroutines.launch

@Composable
fun RecipeView(
    modifier: Modifier = Modifier,
    id:String
) {
   Scaffold {innerpaddin->
       val scroll = rememberScrollState()
       val viewmodel:MainScreenViewmodel  = viewModel(factory = MainScreenViewmodel.factory)
       val favoritesUiState = viewmodel.favoritesUiState.collectAsState()
       SideEffect {
           viewmodel.getDetail(id.toInt())
       }
       val uiStae = viewmodel.detailRecipeUIState.collectAsState()
       val scope  = rememberCoroutineScope()
       when(uiStae.value){
           DetailRecipeUIState.Loading ->{
               Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                   CircularProgressIndicator()
               }
           }
           is DetailRecipeUIState.Sucsess -> {
               Column(modifier = modifier
                   .verticalScroll(scroll)
                   .padding(paddingValues = innerpaddin)){
                   ImageComposable(
                       name = (uiStae.value as DetailRecipeUIState.Sucsess).detail.title,
                       URL = (uiStae.value as DetailRecipeUIState.Sucsess).detail.image,
                       favoritesUiState = favoritesUiState.value,
                       onIconClick = {
                           scope.launch {
                               viewmodel.isfavoterToagal(id)
                           }
                       }
                   )
                   Spacer(modifier = modifier.height(20.dp))
                   Row (horizontalArrangement = Arrangement.SpaceAround){
                       CardComopse(heading = "Ready in", value = (uiStae.value as DetailRecipeUIState.Sucsess).detail.readyInMinutes.toString()+"min")
                       CardComopse(heading = "Servings", value = (uiStae.value as DetailRecipeUIState.Sucsess).detail.servings.toString())
                       CardComopse(heading = "Price/serving", value = (uiStae.value as DetailRecipeUIState.Sucsess).detail.pricePerServing.toString())
                   }
                   Text(
                       text = "Ingredients",
                       fontSize = 19.sp,
                       fontWeight = FontWeight(700),
                       modifier = modifier.padding(start = 10.dp)
                   )
                   LazyRow {
                       items((uiStae.value as DetailRecipeUIState.Sucsess).detail.extendedIngredients){
                           ImageCard(
                               name = it.name,
                               URL = "https://img.spoonacular.com/ingredients_250x250/${it.image}",
                               modifier = modifier.padding(start = 20.dp, top = 20.dp, bottom = 20.dp)
                           )
                       }
                   }
                   Perragraf(
                       heading = "Instructions",
                       bodyString = (uiStae.value as DetailRecipeUIState.Sucsess).detail.instructions.toString(),
                   )
                   Spacer(modifier = modifier.height(20.dp))
                   LazyRow {
                       items((uiStae.value as DetailRecipeUIState.Sucsess).equipments.equipment){
                           ImageCard(
                               name = it.name,
                               URL = "https://img.spoonacular.com/equipment_250x250/${it.image}",
                               modifier = modifier.padding(start = 20.dp, bottom = 20.dp, top = 20.dp)
                           )
                       }
                   }
                   Spacer(modifier = modifier.height(20.dp))
                   Perragraf(
                       heading = "Quick Summary",
                       bodyString = (uiStae.value as DetailRecipeUIState.Sucsess).detail.summary
                   )
                   BadExpandableCard("Bad for health nutrition",(uiStae.value as DetailRecipeUIState.Sucsess).nutritions.bad)
                   GoodExpandableCard(tital = "Good for health nutrition", list = (uiStae.value as DetailRecipeUIState.Sucsess).nutritions.good)
               }
           }

           is DetailRecipeUIState.Error -> {
               Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                   Text(text = (uiStae.value as DetailRecipeUIState.Error).message)
               }
           }
       }
   }

}

@Composable
fun GoodExpandableCard(
    tital: String,
    list: List<Good>,
    modifier: Modifier= Modifier,
) {
    var isExpanded by remember{ mutableStateOf(false) }
    Card(
        modifier =modifier.fillMaxWidth(),
        colors =CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = Color.Black
        )
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.clickable { isExpanded = !isExpanded }
        ){
            Text(
                text = tital,
                modifier = modifier
                    .weight(1f)
                    .padding(start = 10.dp)
            )
            IconButton(onClick = {}) {
                Icon(painter = painterResource(if(isExpanded)R.drawable.down_key else R.drawable.upkeysvg), contentDescription = "")
            }
        }
        AnimatedVisibility (isExpanded){
            Column {
                list.forEach {
                    Row (modifier = modifier.padding(bottom = 10.dp)){
                        Text(
                            text = it.title,
                            modifier = modifier
                                .weight(1f)
                                .padding(start = 10.dp)
                        )
                        Text(
                            text = it.amount,
                            modifier= modifier.padding(end = 10.dp)
                        )
                    }
                    Spacer(modifier = modifier
                        .height(1.dp)
                        .background(Color.Black))
                }
            }
        }
    }
}

@Composable
fun BadExpandableCard(
    tital:String,
    list: List<bad>,
    modifier:Modifier = Modifier
) {
    var isExpanded by remember{ mutableStateOf(false) }
    Card(
        modifier =modifier.fillMaxWidth(),
        colors =CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = Color.Black
        )
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.clickable { isExpanded =!isExpanded }
        ){
            Text(
                text = tital,
                modifier = modifier
                    .weight(1f)
                    .padding(start = 10.dp)
            )
            IconButton(onClick = {isExpanded =!isExpanded}) {
                Icon(painter = painterResource(if(isExpanded)R.drawable.down_key else R.drawable.upkeysvg), contentDescription = "")
            }
        }
            AnimatedVisibility (isExpanded){
                Column {
                    list.forEach {
                        Row (modifier = modifier.padding(bottom = 10.dp)){
                            Text(
                                text = it.title,
                                modifier = modifier
                                    .weight(1f)
                                    .padding(start = 10.dp)
                            )
                            Text(
                                text = it.amount,
                                modifier= modifier.padding(end = 10.dp)
                            )
                        }
                        Spacer(modifier = modifier
                            .height(1.dp)
                            .background(Color.Black))
                    }
                }
            }
        }
    }



@Composable
fun ImageCard(
    name:String,
    URL:String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(bottom = 10.dp)
            .border(2.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp))
    ) {
        AsyncImage(
            model =  URL,
            contentDescription =null,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.loading),
            modifier= modifier
                .size(115.dp)
                .clip(RoundedCornerShape(50))
        )
        Text(
            text = name,
            fontSize = 16.sp,
            fontWeight = FontWeight(500),
            modifier = modifier.padding(start = 10.dp, end = 10.dp)
        )
    }
}


@Composable
fun Perragraf(
    heading:String,
    bodyString: String,
    modifier: Modifier = Modifier
) {
    Column (modifier= modifier.padding(start = 10.dp)){
        Text(
            text = heading,
            fontSize = 19.sp,
            fontWeight = FontWeight(700),
            modifier = modifier.padding(bottom = 10.dp)
        )
        Text(
            text = bodyString,
            fontSize = 19.sp,
            fontWeight = FontWeight(400)
        )
    }
}

@Composable
fun CardComopse(
    heading:String,
    value:String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(start = 10.dp)
            .width(135.dp)
            .height(75.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(2.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = heading,
                color = Color.Gray,
                fontSize = 16.sp
            )
            Text(
                text = value,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight(600),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ImageComposable(
    name:String,
    URL:String,
    favoritesUiState: FavoritesUiState,
    onIconClick:()->Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(360.dp)
            .fillMaxWidth()
    ){
        AsyncImage(
            model =URL,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier= modifier.fillMaxSize()
        )
        when (favoritesUiState){
            is FavoritesUiState.Error -> {
                Text(favoritesUiState.message)
            }
            FavoritesUiState.Loading -> {
                Box(modifier =modifier.size(100.dp), contentAlignment = Alignment.TopEnd){
                    CircularProgressIndicator()
                }
            }
            is FavoritesUiState.Succeed -> {
                    Icon(
                        painter = painterResource(
                            if (favoritesUiState.isFavorite)R.drawable.added
                            else R.drawable.add_to_favourit,
                        ),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = modifier.size(100.dp).align(Alignment.TopEnd).clickable {onIconClick() }
                    )
            }
        }
        /*IconButton(
            onClick = {

            },
            modifier= modifier
                .fillMaxWidth()
                .padding(end = 20.dp)
                .align(Alignment.TopEnd)
        ) {
            Icon(
                painter = painterResource(if(isFavorets) R.drawable.added else R.drawable.add_to_favourit),
                contentDescription = "",
                tint = Color.Unspecified,
                modifier = modifier.size(50.dp)
                    .align(Alignment.TopEnd)
            )
        }*/
        Box(modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    startY = 500f
                )
            ))
        Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart){
            Text(
                text = name,
                color = Color.White,
                fontSize = 27.sp,
                modifier = modifier.padding(bottom = 20.dp, start = 10.dp)
            )
        }
    }
}
