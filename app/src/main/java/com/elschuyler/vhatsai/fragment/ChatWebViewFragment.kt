package com.elschuyler.vhatsai.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.elschuyler.vhatsai.R

/**
 * WebView Fragment for loading AI chat interfaces.
 * 
 * Memory Management (PRD §6.2):
 * - WebView is destroyed when fragment view is destroyed
 * - No WebView pooling in v1 (strict memory limits)
 * - JavaScript paused when fragment is not visible
 */
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
        // Create container layout programmatically
        return inflater.inflate(R.layout.fragment_webview, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        webView = view.findViewById(R.id.webView)
        progressBar = view.findViewById(R.id.progressBar)
        
        setupWebView()
        loadProvider()
    }
    
    private fun WebView.setupWebView() {
        // Enable JavaScript (required for all AI chat UIs)
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true  // For session storage
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        settings.databaseEnabled = true
        settings.allowFileAccess = false  // Security: disable file access
        settings.thirdPartyCookiesEnabled = true  // For AI auth cookies
        
        // Keep navigation inside WebView (don't open external browser)
        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar?.visibility = View.GONE
            }
        }
        
        // Show progress while loading
        webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar?.progress = newProgress
                if (newProgress == 100) {
                    progressBar?.visibility = View.GONE
                } else {
                    progressBar?.visibility = View.VISIBLE
                }
            }
        }
    }
    
    private fun loadProvider() {
        if (providerUrl.isEmpty()) {
            Toast.makeText(requireContext(), "No URL configured", Toast.LENGTH_SHORT).show()
            return
        }
        webView?.loadUrl(providerUrl)
    }
    
    /**
     * Call this when navigating away from chat to free memory.
     * PRD §6.2: WebView must be destroyed to prevent memory leaks.
     */
    fun destroyWebView() {
        webView?.stopLoading()
        webView?.removeAllViews()
        webView?.destroy()
        webView = null
    }
    
    override fun onPause() {
        super.onPause()
        // Pause JavaScript timers when fragment is not visible (PRD §7)
        webView?.onPause()
    }
    
    override fun onResume() {
        super.onResume()
        // Resume JavaScript timers
        webView?.onResume()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        // CRITICAL: Destroy WebView to free memory (PRD §6.2)
        destroyWebView()
        progressBar = null
    }
}
