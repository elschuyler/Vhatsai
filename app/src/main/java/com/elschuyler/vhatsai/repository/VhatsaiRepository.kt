package com.elschuyler.vhatsai.repository

import com.elschuyler.vhatsai.db.VhatsaiDatabase
import com.elschuyler.vhatsai.db.entity.Chat
import com.elschuyler.vhatsai.db.entity.Message
import com.elschuyler.vhatsai.db.entity.AIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class VhatsaiRepository(private val database: VhatsaiDatabase) {
    fun getAllChats(): Flow<List<Chat>> = database.chatDao().getAllChats()
    fun getMessagesByChatId(chatId: Long): Flow<List<Message>> = database.messageDao().getMessagesByChatId(chatId)
    fun getAllActiveServices(): Flow<List<AIService>> = database.aiServiceDao().getAllActiveServices()

    suspend fun getChatById(id: Long): Chat? = withContext(Dispatchers.IO) {
        database.chatDao().getChatById(id)
    }

    suspend fun insertChat(chat: Chat): Long = withContext(Dispatchers.IO) {
        database.chatDao().insertChat(chat)
    }

    suspend fun insertMessage(message: Message) = withContext(Dispatchers.IO) {
        database.messageDao().insertMessage(message)
    }

    suspend fun updateChat(chat: Chat) = withContext(Dispatchers.IO) {
        database.chatDao().updateChat(chat)
    }

    suspend fun updateLastMessage(chatId: Long, lastMessage: String, timestamp: Long) = withContext(Dispatchers.IO) {
        database.chatDao().updateLastMessage(chatId, lastMessage, timestamp)
    }

    suspend fun markPendingUserMessagesAsCompleted(chatId: Long) = withContext(Dispatchers.IO) {
        database.messageDao().markPendingUserMessagesAsCompleted(chatId)
    }
}
