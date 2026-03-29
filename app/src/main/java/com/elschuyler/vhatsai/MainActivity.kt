package com.elschuyler.vhatsai

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.elschuyler.vhatsai.fragment.ChatWebViewFragment
import com.elschuyler.vhatsai.fragment.InboxFragment
import com.elschuyler.vhatsai.databinding.ActivityMainBinding

/**
 * Main Activity - Hosts fragment navigation.
 * 
 * Architecture:
 * - Single Activity, Multiple Fragments
 * - InboxFragment: Chat list (WhatsApp-style)
 * - ChatWebViewFragment: Individual AI chat (WebView)
 */
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private var currentWebViewFragment: ChatWebViewFragment? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        
        // Load inbox on first launch
        if (savedInstanceState == null) {
            loadInbox()
        }
    }
    
    /**
     * Load the inbox/fragment_chat list fragment.
     */
    private fun loadInbox() {
        currentWebViewFragment = null
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, InboxFragment())
            .commit()
        
        supportActionBar?.title = getString(R.string.app_name)
    }
    
    /**
     * Open a chat with specific AI provider.
     * 
     * @param url The AI provider's web chat URL
     * @param name Display name for the provider
     */
    fun openChat(url: String, name: String) {
        // Destroy any existing WebView first (memory management)
        currentWebViewFragment?.destroyWebView()
        
        val fragment = ChatWebViewFragment.newInstance(url, name)
        currentWebViewFragment = fragment
        
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
        
        supportActionBar?.title = name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Back button pressed
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                    currentWebViewFragment = null
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    supportActionBar?.title = getString(R.string.app_name)
                } else {
                    loadInbox()
                }
                true
            }
            R.id.action_settings -> {
                Toast.makeText(this, "Settings coming soon", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            // Destroy WebView before navigating back (PRD §6.2)
            currentWebViewFragment?.destroyWebView()
            currentWebViewFragment = null
            supportFragmentManager.popBackStack()
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.title = getString(R.string.app_name)
        } else {
            super.onBackPressed()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Ensure WebView is destroyed when activity is destroyed
        currentWebViewFragment?.destroyWebView()
        currentWebViewFragment = null
    }
}
