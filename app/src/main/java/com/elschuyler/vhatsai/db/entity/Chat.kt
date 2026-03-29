package com.elschuyler.vhatsai.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import java.util.UUID

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = Chat::class,
            parentColumns = ["id"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Message(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val chatId: Long, // Foreign key to Chat
    val role: String, // "user" or "ai"
    val content: String,
    val timestamp: Long, // milliseconds since epoch
    val isPending: Boolean = false // for user messages waiting for AI response
)
