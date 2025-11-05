package com.example.data.common.di.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.remote.local.database.example.dao.ExampleDao
import com.example.data.remote.local.database.example.entity.ExampleEntity
import com.example.data.remote.local.database.nonfeature.terms.TermDetailsDao
import com.example.data.remote.local.database.nonfeature.terms.TermDetailsEntity

@Database(
    entities = [ExampleEntity::class, TermDetailsEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exampleDao(): ExampleDao

    abstract fun termDetailsDao(): TermDetailsDao
}