package com.bagasbest.mywebview;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView myWebView = findViewById(R.id.webView);
        myWebView.getSettings().setJavaScriptEnabled(true);

        myWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:alert('Web ITERA berhasil dimuat')");
            }
        });

        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                result.confirm();
                return true;
            }
        });

        /*Kode di atas digunakan untuk memuat sebuah url dari website.
        Perlu Anda ketahui bahwa untuk OS Nougat ke atas, sebuah url harus menggunakan https.
        Jika Anda tidak menggunakan https atau hanya http saja,
        maka alamat tersebut tidak akan dimuat. */

        myWebView.loadUrl("https://www.itera.ac.id");


    }
}