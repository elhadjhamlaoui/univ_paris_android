package com.example.projetcomposants

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView

class ViewLink : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_link)
        val myWebView: WebView = findViewById(R.id.webView)
        myWebView.loadUrl(intent.getStringExtra("link"))
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        setTitle(intent.getStringExtra("name"))
    }
}