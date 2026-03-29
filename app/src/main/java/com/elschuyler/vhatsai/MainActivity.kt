package com.elschuyler.vhatsai

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.elschuyler.vhatsai.databinding.ActivityMainBinding
import com.elschuyler.vhatsai.fragment.ChatFragment
import com.elschuyler.vhatsai.fragment.InboxFragment
import com.elschuyler.vhatsai.viewmodel.ChatViewModel
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val chatViewModel: ChatViewModel by viewModels()

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
        replaceFragment(InboxFragment(), "inbox")
        supportActionBar?.title = getString(R.string.app_name)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    fun openChat(chatId: Long, aiProviderId: String) {
        val fragment = ChatFragment.newInstance(chatId, aiProviderId)
        replaceFragment(fragment, "chat_$chatId")
        supportActionBar?.title = aiProviderId // Use provider name or chat title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun replaceFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment, tag)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
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
            supportFragmentManager.popBackStack()
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.title = getString(R.string.app_name)
        } else {
            super.onBackPressed()
        }
    }
}
