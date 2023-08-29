package com.example.wnsplatform.DataClassNews

data class Articles (
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage:String,
    val content:String,
    val source:Source
        ):java.io.Serializable
