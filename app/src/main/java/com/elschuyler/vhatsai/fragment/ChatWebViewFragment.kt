package com.elschuyler.vhatsai.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elschuyler.vhatsai.R
import com.elschuyler.vhatsai.db.entity.Chat
import com.elschuyler.vhatsai.db.entity.Message
import com.elschuyler.vhatsai.viewmodel.ChatViewModel

class ChatFragment : Fragment() {
    private val viewModel: ChatViewModel by activityViewModels()
    private lateinit var messagesRecyclerView: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var adapter: MessageAdapter

    private var chatId: Long = -1
    private var aiProviderId: String = ""

    companion object {
        private const val ARG_CHAT_ID = "chat_id"
        private const val ARG_AI_PROVIDER_ID = "ai_provider_id"

        fun newInstance(chatId: Long, aiProviderId: String): ChatFragment {
            val fragment = ChatFragment()
            val args = Bundle().apply {
                putLong(ARG_CHAT_ID, chatId)
                putString(ARG_AI_PROVIDER_ID, aiProviderId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chatId = it.getLong(ARG_CHAT_ID)
            aiProviderId = it.getString(ARG_AI_PROVIDER_ID) ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messagesRecyclerView = view.findViewById(R.id.messagesRecyclerView)
        messageInput = view.findViewById(R.id.messageInput)
        sendButton = view.findViewById(R.id.sendButton)

        setupRecyclerView()
        setupClickListeners()

        // Observe messages for this chat
        viewModel.currentChat.observe(viewLifecycleOwner) { chat ->
            if (chat?.id == chatId) {
                // Subscribe to messages for this chat
                subscribeToMessages()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = MessageAdapter()
        messagesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        messagesRecyclerView.adapter = adapter
    }

    private fun setupClickListeners() {
        sendButton.setOnClickListener {
            val message = messageInput.text.toString().trim()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(message, aiProviderId)
                messageInput.setText("")
            }
        }
    }

    private fun subscribeToMessages() {
        // In a real app, you'd observe the messages flow from the repository
        // For now, we'll just set a placeholder
        adapter.submitList(listOf(
            Message(chatId = chatId, role = "ai", content = "Hello! How can I assist you today?", timestamp = System.currentTimeMillis() - 10000),
            Message(chatId = chatId, role = "user", content = "Hi, can you tell me about AI?", timestamp = System.currentTimeMillis() - 5000)
        ))
    }
}

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    private var messages = listOf<Message>()

    fun submitList(newList: List<Message>) {
        messages = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount() = messages.size

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageContainer: View = itemView.findViewById(R.id.messageContainer)
        private val messageText: android.widget.TextView = itemView.findViewById(R.id.messageText)
        private val timestampText: android.widget.TextView = itemView.findViewById(R.id.timestampText)

        fun bind(message: Message) {
            messageText.text = message.content

            // Format timestamp
            val sdf = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
            timestampText.text = sdf.format(java.util.Date(message.timestamp))

            // Style based on role
            if (message.role == "user") {
                messageContainer.setBackgroundResource(R.drawable.bg_bubble_user) // You'll need to create this
                messageContainer.setBackgroundColor(android.graphics.Color.parseColor("#1A2860")) // User bubble color
            } else {
                messageContainer.setBackgroundResource(R.drawable.bg_bubble) // Default AI bubble
                messageContainer.setBackgroundColor(android.graphics.Color.WHITE) // AI bubble color
            }
        }
    }
}
