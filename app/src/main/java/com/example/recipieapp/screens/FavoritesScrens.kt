package com.example.recipieapp.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.recipieapp.R
import com.example.recipieapp.model.BulkFavoriteModulItem
import com.example.recipieapp.viewmodel.FavoritesScreenViewModel
import com.example.recipieapp.viewmodel.FavoritesUIStat

@Composable
fun FavoritesScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: FavoritesScreenViewModel = viewModel(factory =FavoritesScreenViewModel.factory)
) {
    val Stat by viewModel.favoritesUIStat.collectAsState()
    Box(
        modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ){

        when(Stat){
            is FavoritesUIStat.Error -> {
                Text((Stat as FavoritesUIStat.Error).message)
            }
            FavoritesUIStat.Loading -> {
                Box(
                    modifier = modifier.fillMaxWidth().size(100.dp),
                    contentAlignment = Alignment.TopEnd,
                ){
                    CircularProgressIndicator()
                }
            }
            is FavoritesUIStat.Success -> {
                LazyColumn(
                    contentPadding = PaddingValues(start = 10.dp , end = 10.dp , bottom = 10.dp),
                    modifier = modifier.align(Alignment.TopEnd).padding(top = 50.dp)
                ) {
                    items( items = (Stat as FavoritesUIStat.Success).list,){
                        MainBody(it, { navHostController.navigate(Screens.RecipeView.toString()+"/$it") })
                    }
                }
            }
        }
    }
}

@Composable
private fun MainBody(
    bulkFavoriteModulItem: BulkFavoriteModulItem,
    onItemClick:(String)->Unit,
    modifier: Modifier = Modifier
) {
    Row (
        modifier= modifier
            .clickable { onItemClick(bulkFavoriteModulItem.id.toString()) }
            .padding(start = 10.dp, end = 20.dp, bottom = 20.dp)
            .fillMaxWidth()
            .border(2.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
    ){
        AsyncImage(
            model = bulkFavoriteModulItem.image,
            contentDescription = bulkFavoriteModulItem.summary,
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
            Text(text = bulkFavoriteModulItem.title)
            Text(text = "Ready in ${bulkFavoriteModulItem.readyInMinutes} min")
        }
    }
}