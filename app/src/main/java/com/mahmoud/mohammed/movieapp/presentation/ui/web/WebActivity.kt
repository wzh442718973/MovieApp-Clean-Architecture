package com.mahmoud.mohammed.movieapp.presentation.ui.web;

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.JsResult
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.mahmoud.mohammed.movieapp.R
import com.mahmoud.mohammed.movieapp.databinding.ActivityWebBinding


val KEY_TITLE = "key_title"

class WebActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun openWeb(context: Context, title: String?, url: String?) {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(KEY_TITLE, title)
            intent.setData(Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityWebBinding.inflate(layoutInflater);
        setContentView(binding.root)
        setSupportActionBar(binding.myToolbar)

        title = getString(R.string.title_privacy)

        var _title = intent.getStringExtra(KEY_TITLE)
        if (_title != null) {
            title = _title
        }
        if (intent.data == null) {
            finish()
        }
        initWebView(binding.webView)
        binding.webView.loadUrl(intent.data.toString())
    }


    private val viewClient: WebViewClient = object : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            try {
                if (url.startsWith("http")) {
                    view.loadUrl(url)
                } else {
                    val uri = Uri.parse(url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            } catch (e: Throwable) {
                view.loadUrl(url)
            }
            return true
        }

        override fun onReceivedSslError(
            webView: WebView, sslErrorHandler: SslErrorHandler, sslError: SslError
        ) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this@WebActivity)
            builder.setMessage("SSL Error!")
            builder.setPositiveButton(android.R.string.ok) { dialog, which -> sslErrorHandler.proceed() }
            builder.setNegativeButton(android.R.string.cancel) { dialog, which -> sslErrorHandler.cancel() }
            builder.create().show()
        }

        override fun onPageFinished(webView: WebView, str: String) {
            super.onPageFinished(webView, str)
        }

        override fun onReceivedError(webView: WebView, i: Int, str: String, str2: String) {
            webView.stopLoading()
            webView.loadUrl("javascript:document.body.innerHtml=\" \"")
        }
    }

    private val chromeClient: WebChromeClient = object : WebChromeClient() {
        override fun onJsBeforeUnload(
            view: WebView, url: String, message: String, result: JsResult
        ): Boolean {
            result.confirm()
            return true
        }

    }

    private fun initWebView(webView: WebView) {
        webView.setInitialScale(0)
        webView.webViewClient = viewClient
        webView.webChromeClient = chromeClient
        val mSettings = webView.settings
        mSettings.builtInZoomControls = false
        mSettings.setSupportZoom(false)
        mSettings.useWideViewPort = true
        mSettings.javaScriptEnabled = true
        mSettings.allowContentAccess = true
        mSettings.allowFileAccess = true
        mSettings.allowFileAccessFromFileURLs = true
        mSettings.setSupportMultipleWindows(true)
        mSettings.loadsImagesAutomatically = true
        mSettings.javaScriptCanOpenWindowsAutomatically = true
        mSettings.domStorageEnabled = true
        if (Build.VERSION.SDK_INT >= 21) {
            val instance: CookieManager = CookieManager.getInstance()
            instance.setAcceptCookie(true)
            instance.setAcceptThirdPartyCookies(webView, true)
        }

        // mSettings.setSupportMultipleWindows(true);
        //mSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        mSettings.setAppCacheMaxSize(5 * 1048576);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            mSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        mSettings.blockNetworkImage = false

//        mSettings.setUserAgentString("Mozilla/5.0 (Linux; U; Android 12; zh-cn; 2109119BC Build/SKQ1.211006.001) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/89.0.4389.116 Mobile Safari/537.36 XiaoMi/MiuiBrowser/16.8.58 swan-mibrowser");

        // mSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        if (Build.VERSION.SDK_INT >= 16) {
            mSettings.allowFileAccessFromFileURLs = true
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mSettings.allowUniversalAccessFromFileURLs = true
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mSettings.mediaPlaybackRequiresUserGesture = false
        }
    }

}
