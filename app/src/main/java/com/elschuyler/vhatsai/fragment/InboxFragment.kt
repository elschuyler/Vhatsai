package com.elschuyler.vhatsai.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elschuyler.vhatsai.MainActivity
import com.elschuyler.vhatsai.R

class InboxFragment : Fragment() {

    private val aiProviders = listOf(
        AIProvider("ChatGPT", "https://chat.openai.com"),
        AIProvider("Claude", "https://claude.ai"),
        AIProvider("Gemini", "https://gemini.google.com"),
        AIProvider("Mistral", "https://chat.mistral.ai")
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
            (activity as? MainActivity)?.openChat(provider.url, provider.name)
        }
    }

    data class AIProvider(
        val name: String,
        val url: String
    )

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
