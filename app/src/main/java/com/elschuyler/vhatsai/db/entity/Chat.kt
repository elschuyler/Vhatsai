package com.elschuyler.vhatsai.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class Chat(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val aiProviderId: String, // e.g., "chatgpt", "claude"
    val aiProviderName: String, // e.g., "ChatGPT", "Claude"
    val title: String, // e.g., "My First Chat", "About AI..."
    val lastMessage: String?,
    val lastUpdated: Long, // timestamp
    val createdAt: Long // timestamp
)
