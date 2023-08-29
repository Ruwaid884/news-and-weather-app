package com.example.wnsplatform.Network

import androidx.core.provider.FontsContractCompat.FontRequestCallback.FontRequestFailReason
import com.example.wnsplatform.DataClassNews.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("top-headlines")
    fun getNews(


        @Query("country") country:String,
        @Query("category") category:String,
        @Query("pageSize") pageSize:Int,
        @Query("apikey") apikey:String
    ): Call<NewsResponse>
}