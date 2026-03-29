package com.elschuyler.vhatsai.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.elschuyler.vhatsai.db.dao.ChatDao
import com.elschuyler.vhatsai.db.dao.MessageDao
import com.elschuyler.vhatsai.db.dao.AIServiceDao
import com.elschuyler.vhatsai.db.entity.Chat
import com.elschuyler.vhatsai.db.entity.Message
import com.elschuyler.vhatsai.db.entity.AIService
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteOpenHelper
import net.sqlcipher.database.SupportFactory
import java.security.SecureRandom

@Database(
    entities = [Chat::class, Message::class, AIService::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class VhatsaiDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao
    abstract fun aiServiceDao(): AIServiceDao

    companion object {
        @Volatile
        private var INSTANCE: VhatsaiDatabase? = null

        fun getDatabase(context: Context): VhatsaiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VhatsaiDatabase::class.java,
                    "vhatsai_database"
                )
                .openHelperFactory(createSupportFactory(getEncryptionKey()))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private fun getEncryptionKey(): String {
            // In a real app, retrieve securely from Android Keystore
            // For now, using a default key (not recommended for production)
            return "vhatsai_default_key_2026".padEnd(32, '\u0000').substring(0, 32)
        }

        private fun createSupportFactory(key: String): SupportFactory {
            val passphrase: ByteArray = SQLiteDatabase.getBytes(key.toCharArray())
            val(passphrase)
            return factory
        }
    }
}
