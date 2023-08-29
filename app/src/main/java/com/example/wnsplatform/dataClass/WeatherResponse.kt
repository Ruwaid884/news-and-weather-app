package com.example.wnsplatform.dataClass

data class WeatherResponse(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val dt: Int,
    val clouds: Clouds,
    val sys : Sys,
    val id: Int,
    val name: String,
    val cod:Int
):java.io.Serializable