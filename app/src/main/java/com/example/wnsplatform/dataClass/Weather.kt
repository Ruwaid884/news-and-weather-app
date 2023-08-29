package com.example.wnsplatform.dataClass

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
):java.io.Serializable
