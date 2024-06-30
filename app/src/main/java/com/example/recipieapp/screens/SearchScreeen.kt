package com.example.recipieapp.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SearchScreeen(modifier: Modifier = Modifier) {
    Scaffold {innerpadding->
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = modifier.padding(paddingValues = innerpadding)
        )
    }
}