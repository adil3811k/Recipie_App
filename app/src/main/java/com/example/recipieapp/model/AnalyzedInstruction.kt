package com.example.recipieapp.model

data class AnalyzedInstruction(
    val name: String,
    val steps: List<Step>
)