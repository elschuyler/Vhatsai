package com.elschuyler.vhatsai.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.elschuyler.vhatsai.R

class ChatWebViewFragment : Fragment() {

    private var webView: WebView? = null
    private var progressBar: ProgressBar? = null
    private var providerUrl: String = ""
    private var providerName: String = ""

    companion object {
        private const val ARG_URL = "provider_url"
        private const val ARG_NAME = "provider_name"

        fun newInstance(providerUrl: String, providerName: String) = ChatWebViewFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_URL, providerUrl)
                putString(ARG_NAME, providerName)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        providerUrl = arguments?.getString(ARG_URL) ?: ""
        providerName = arguments?.getString(ARG_NAME) ?: "AI Chat"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_webview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView = view.findViewById(R.id.webView)
        progressBar = view.findViewById(R.id.progressBar)

        setupWebView()
        loadProvider()
    }

    private fun setupWebView() {
        webView?.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.databaseEnabled = true
            settings.allowFileAccess = false
            settings.thirdPartyCookiesEnabled = true

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    progressBar?.visibility = View.GONE
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    progressBar?.progress = newProgress
                    progressBar?.visibility = if (newProgress == 100) View.GONE else View.VISIBLE
                }
            }
        }
    }

    private fun loadProvider() {
        if (providerUrl.isNotEmpty()) {
            webView?.loadUrl(providerUrl)
        }
    }

    fun destroyWebView() {
        webView?.stopLoading()
        webView?.removeAllViews()
        webView?.destroy()
        webView = null
    }

    override fun onPause() {
        super.onPause()
        webView?.onPause()
    }

    override fun onResume() {
        super.onResume()
        webView?.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        destroyWebView()
        progressBar = null
    }
}
