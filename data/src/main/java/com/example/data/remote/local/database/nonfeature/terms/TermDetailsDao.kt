package com.example.data.remote.local.database.nonfeature.terms

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface TermDetailsDao {

    @Upsert
    suspend fun upsertTermDetails(termDetailsEntity: TermDetailsEntity)

    @Query("SELECT * FROM termDetails")
    suspend fun getAllTermDetails(): List<TermDetailsEntity>

    @Query("SELECT * FROM termDetails WHERE id = :id")
    suspend fun getTermDetailsById(id: String): TermDetailsEntity
}