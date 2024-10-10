package com.example.recipieapp.screens

import Rout
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.recipieapp.R
import com.example.recipieapp.newtwork.Suggetion
import com.example.recipieapp.ui.theme.RecipieAppTheme
import com.example.recipieapp.viewmodel.MainScreenViewmodel
import com.example.recipieapp.viewmodel.SuggetionUIState

@Composable
fun SearchScreeen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {

    var test  by remember{ mutableStateOf("") }
    val viewModel   = viewModel<MainScreenViewmodel>(factory = MainScreenViewmodel.factory)
    val state by viewModel.suggetionUIState.collectAsState()
    Scaffold {innerpadding->
        Column(modifier= modifier.padding(paddingValues = innerpadding)) {
            testfild(text = test, onValueChange = {
                test = it
                viewModel.getsuggetions(test)
            })
            when(state){
                is SuggetionUIState.Error -> {
                    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        Text(text = (state as SuggetionUIState.Error).message)
                    }
                }
                SuggetionUIState.Ideal -> {
                    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        Text(text = "Please First Search ")
                    }
                }
                SuggetionUIState.Loading -> {
                    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        CircularProgressIndicator()
                    }
                }
                is SuggetionUIState.Sucsess -> {
                    LazyColumn {
                        items((state as SuggetionUIState.Sucsess).suggetions){
                            cardView(
                                suggetion = it,
                                onItemClick = {id->
                                    navHostController.navigate(Rout.RecipeView(id))
                                })
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun testfild(
    text:String,
    onValueChange:(String)->Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = text,
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "") },
        onValueChange = onValueChange,
        colors = TextFieldDefaults.colors(unfocusedContainerColor = MaterialTheme.colorScheme.secondary, unfocusedLabelColor = Color.Black),
        modifier = Modifier
            .padding(end = 20.dp)
            .fillMaxWidth()
            .height(53.dp)
            .clip(RoundedCornerShape(16.dp))
    )
}


@Composable
fun cardView(
    suggetion: Suggetion,
    onItemClick:(String)->Unit,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick(suggetion.id.toString()) },
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            painter = painterResource(id = R.drawable.cardicon),
            contentDescription = "",
            modifier = modifier
                .padding(10.dp)
                .size(20.dp)

        )

        Text(
            text = suggetion.title,
            fontSize = 20.sp,
            modifier = modifier.fillMaxWidth()
        )
    }
}
