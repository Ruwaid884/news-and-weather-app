package com.example.wnsplatform.dataClass

data class Main (
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val sea_level:Int,
    val grnd_level:Int
        ): java.io.Serializable
