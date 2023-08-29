package com.example.wnsplatform.DataClassNews

data class NewsResponse (
    val status: String,
    val totalResults: Int,
    val articles: List<Articles>
    ):java.io.Serializable