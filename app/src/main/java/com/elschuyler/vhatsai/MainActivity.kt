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

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private var currentWebViewFragment: ChatWebViewFragment? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        
        if (savedInstanceState == null) {
            loadInbox()
        }
    }
    
    private fun loadInbox() {
        currentWebViewFragment = null
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, InboxFragment())
            .commit()
        
        supportActionBar?.title = getString(R.string.app_name)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
    
    /**
     * Open chat with AI provider.
     * Destroys previous WebView first (memory management).
     */
    fun openChat(url: String, name: String) {
        // Destroy existing WebView before opening new one
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
                // Back button: destroy WebView, return to inbox
                currentWebViewFragment?.destroyWebView()
                currentWebViewFragment = null
                supportFragmentManager.popBackStack()
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                supportActionBar?.title = getString(R.string.app_name)
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
        // Ensure cleanup
        currentWebViewFragment?.destroyWebView()
        currentWebViewFragment = null
    }
}
