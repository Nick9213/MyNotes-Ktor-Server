package com.example.models

data class Note(
    val id:String,
    val userEmail:String,
    val noteTitle:String,
    val description:String,
    val date:Long
)
