package com.elschuyler.vhatsai

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.elschuyler.vhatsai.databinding.ActivityMainBinding
import com.elschuyler.vhatsai.fragment.ChatWebViewFragment
import com.elschuyler.vhatsai.fragment.InboxFragment

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

    fun openChat(url: String, name: String) {
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
        currentWebViewFragment?.destroyWebView()
        currentWebViewFragment = null
    }
}
