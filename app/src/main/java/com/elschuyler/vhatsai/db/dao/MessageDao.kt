package com.elschuyler.vhatsai.db.dao

import androidx.room.*
import com.elschuyler.vhatsai.db.entity.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp ASC")
    fun getMessagesByChatId(chatId: Long): Flow<List<Message>>

    @Query("SELECT * FROM messages WHERE chatId = :chatId AND id = :messageId")
    suspend fun getMessageById(chatId: Long, messageId: String): Message?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)

    @Update
    suspend fun updateMessage(message: Message)

    @Delete
    suspend fun deleteMessage(message: Message)

    @Query("DELETE FROM messages WHERE chatId = :chatId")
    suspend fun deleteMessagesByChatId(chatId: Long)

    @Query("UPDATE messages SET isPending = 0 WHERE chatId = :chatId AND role = 'user' AND isPending = 1")
    suspend fun markPendingUserMessagesAsCompleted(chatId: Long)
}
