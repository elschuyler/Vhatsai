package com.elschuyler.vhatsai.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elschuyler.vhatsai.MainActivity
import com.elschuyler.vhatsai.R

/**
 * Inbox Fragment - Shows list of AI chat providers.
 * 
 * v1: Static list of supported AI providers.
 * v2: Dynamic list from local database + config.
 */
class InboxFragment : Fragment() {
    
    // Sample AI providers for v1 (PRD §2.1: 2-3 supported services)
    private val aiProviders = listOf(
        AIProvider("ChatGPT", "https://chat.openai.com", R.drawable.ic_chatgpt),
        AIProvider("Claude", "https://claude.ai", R.drawable.ic_claude),
        AIProvider("Gemini", "https://gemini.google.com", R.drawable.ic_gemini)
    )
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inbox, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = InboxAdapter(aiProviders) { provider ->
            // Open chat when item clicked
            (activity as? MainActivity)?.openChat(provider.url, provider.name)
        }
    }
    
    data class AIProvider(
        val name: String,
        val url: String,
        val iconRes: Int
    )
    
    inner class InboxAdapter(
        private val providers: List<AIProvider>,
        private val onClick: (AIProvider) -> Unit
    ) : RecyclerView.Adapter<InboxAdapter.ViewHolder>() {
        
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bind(provider: AIProvider) {
                itemView.setOnClickListener { onClick(provider) }
                // TODO: Bind name, icon, last message
            }
        }
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat, parent, false)
            return ViewHolder(view)
        }
        
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(providers[position])
        }
        
        override fun getItemCount() = providers.size
    }
}
