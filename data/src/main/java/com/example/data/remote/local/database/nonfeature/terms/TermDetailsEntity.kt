package com.example.data.remote.local.database.nonfeature.terms

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "termDetails")
data class TermDetailsEntity(
    @PrimaryKey val id: String,
    val title: String,
    val required: Boolean,
    val version: String,
    val content: String
)