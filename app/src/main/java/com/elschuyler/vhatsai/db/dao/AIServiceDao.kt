package com.elschuyler.vhatsai.db.dao

import androidx.room.*
import com.elschuyler.vhatsai.db.entity.AIService
import kotlinx.coroutines.flow.Flow

@Dao
interface AIServiceDao {
    @Query("SELECT * FROM ai_services WHERE isActive = 1 ORDER BY name ASC")
    fun getAllActiveServices(): Flow<List<AIService>>

    @Query("SELECT * FROM ai_services WHERE id = :id")
    suspend fun getServiceById(id: String): AIService?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: AIService)

    @Update
    suspend fun updateService(service: AIService)

    @Delete
    suspend fun deleteService(service: AIService)
}
