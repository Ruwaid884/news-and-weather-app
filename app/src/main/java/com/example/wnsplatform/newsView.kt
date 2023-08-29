package com.example.wnsplatform

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient

class newsView : AppCompatActivity() {

    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_view)

        val str : String? = "https://www.youtube.com"
        val url :String? = intent.getStringExtra("url")
        webView = findViewById(R.id.webView)

        webView.loadUrl("$url")
        }

}