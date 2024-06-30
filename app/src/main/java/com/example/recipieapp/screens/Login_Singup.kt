package com.example.recipieapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.recipieapp.R
import com.example.recipieapp.ui.theme.RecipieAppTheme

@Composable
fun Login_SingUpScreen(
    modifier: Modifier = Modifier,
    onSIngin:()->Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ){
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = modifier.fillMaxSize(1f)
        )
        Column (
            modifier = modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Bottom
        ){
            Text(
                text = "Welcome to ",
                fontSize = 68.sp,
                color = Color.White,
                style = TextStyle(lineHeight = 74.sp),
                fontWeight = FontWeight(200)
            )
            Text(
                text = "Reciipiie",
                fontSize = 68.sp,
                color = Color.White,
                style = TextStyle(lineHeight = 74.sp),
                fontWeight = FontWeight(400)
            )
            Text(
                text = "Please signing to continue",
                color = Color.White,
            )
            Button(
                modifier = modifier
                    .padding(vertical = 10.dp),
                shape = RoundedCornerShape(12.dp),
                onClick = onSIngin
            ) {
                Row (
                    modifier= modifier
                        .height(48.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.google_logo_search_new_svgrepo_com),
                        contentDescription =null,
                        modifier = modifier
                            .height(40.dp)
                            .padding(end = 10.dp)
                    )
                    Text(
                        text = "Continue with google",
                        color = Color.White,
                        fontSize = 15.sp
                    )
                }
            }

        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun priviwer() {
    val navController = rememberNavController()
    RecipieAppTheme {
        Login_SingUpScreen{}
    }
}