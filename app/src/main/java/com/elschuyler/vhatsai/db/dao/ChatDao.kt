package com.elschuyler.vhatsai.db.dao

import androidx.room.*
import com.elschuyler.vhatsai.db.entity.Chat
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats ORDER BY lastUpdated DESC")
    fun getAllChats(): Flow<List<Chat>>

    @Query("SELECT * FROM chats WHERE id = :id")
    suspend fun getChatById(id: Long): Chat?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: Chat): Long

    @Update
    suspend fun updateChat(chat: Chat)

    @Delete
    suspend fun deleteChat(chat: Chat)

    @Query("DELETE FROM chats WHERE id = :id")
    suspend fun deleteChatById(id: Long)

    @Query("UPDATE chats SET lastMessage = :lastMessage, lastUpdated = :timestamp WHERE id = :chatId")
    suspend fun updateLastMessage(chatId: Long, lastMessage: String, timestamp: Long)
}
