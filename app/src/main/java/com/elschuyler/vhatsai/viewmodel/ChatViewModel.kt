package com.elschuyler.vhatsai.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.elschuyler.vhatsai.VhatsaiApplication
import com.elschuyler.vhatsai.db.entity.Chat
import com.elschuyler.vhatsai.db.entity.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as VhatsaiApplication
    private val _currentChat = MutableStateFlow<Chat?>(null)
    val currentChat: StateFlow<Chat?> = _currentChat.asStateFlow()

    fun setCurrentChat(chat: Chat) {
        _currentChat.value = chat
    }

    fun sendMessage(content: String, aiProviderId: String) {
        viewModelScope.launch {
            val chat = _currentChat.value ?: return@launch

            // Insert user message
            val userMessage = Message(
                chatId = chat.id,
                role = "user",
                content = content,
                timestamp = Date().time,
                isPending = true
            )
            app.repository.insertMessage(userMessage)

            // Update chat's last message
            app.repository.updateLastMessage(chat.id, content, Date().time)

            // Here you would typically trigger WebView automation
            // For now, simulate AI response after a delay
            simulateAIResponse(chat.id, aiProviderId, content)
        }
    }

    private fun simulateAIResponse(chatId: Long, aiProviderId: String, userMessage: String) {
        viewModelScope.launch {
            // Simulate AI processing delay
            kotlinx.coroutines.delay(1000)

            // Insert AI response
            val aiResponse = Message(
                chatId = chatId,
                role = "ai",
                content = "This is a simulated response to: \"$userMessage\"",
                timestamp = Date().time
            )
            app.repository.insertMessage(aiResponse)

            // Mark user message as completed
            app.repository.markPendingUserMessagesAsCompleted(chatId)

            // Update chat's last message
            app.repository.updateLastMessage(chatId, aiResponse.content, Date().time)
        }
    }
}
