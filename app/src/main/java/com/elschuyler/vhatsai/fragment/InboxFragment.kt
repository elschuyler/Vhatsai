package com.elschuyler.vhatsai.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elschuyler.vhatsai.MainActivity
import com.elschuyler.vhatsai.R
import com.elschuyler.vhatsai.VhatsaiApplication
import com.elschuyler.vhatsai.db.entity.Chat
import com.elschuyler.vhatsai.viewmodel.ChatViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class InboxFragment : Fragment() {
    private val app by lazy { requireActivity().application as VhatsaiApplication }
    private val chatViewModel: ChatViewModel by activityViewModels()

    private val aiProviders = listOf(
        AIProvider("ChatGPT", "https://chat.openai.com", "chatgpt"),
        AIProvider("Claude", "https://claude.ai", "claude"),
        AIProvider("Gemini", "https://gemini.google.com", "gemini"),
        AIProvider("Mistral", "https://chat.mistral.ai", "mistral")
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
            createOrOpenChat(provider)
        }
    }

    private fun createOrOpenChat(provider: AIProvider) {
        // In a real app, you might check if a chat already exists for this provider
        // For now, create a new one each time
        CoroutineScope(Dispatchers.IO).launch {
            val newChat = Chat(
                aiProviderId = provider.id,
                aiProviderName = provider.name,
                title = provider.name,
                lastMessage = null,
                lastUpdated = Date().time,
                createdAt = Date().time
            )
            val chatId = app.repository.insertChat(newChat)
            
            // Update ViewModel and navigate
            chatViewModel.setCurrentChat(newChat)
            (activity as? MainActivity)?.openChat(chatId, provider.id)
        }
    }

    data class AIProvider(val name: String, val url: String, val id: String)

    inner class InboxAdapter(
        private val providers: List<AIProvider>,
        private val onClick: (AIProvider) -> Unit
    ) : RecyclerView.Adapter<InboxAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val nameView: TextView = view.findViewById(R.id.name)
            fun bind(provider: AIProvider) {
                nameView.text = provider.name
                itemView.setOnClickListener { onClick(provider) }
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
