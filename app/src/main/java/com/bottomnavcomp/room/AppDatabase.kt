package com.bottomnavcomp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bottomnavcomp.models.News

@Database(entities = [News::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}