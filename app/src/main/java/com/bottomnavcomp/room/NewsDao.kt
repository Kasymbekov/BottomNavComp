package com.bottomnavcomp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bottomnavcomp.models.News

@Dao
interface NewsDao {

    @Query("SELECT * FROM news order by createdAt desc")
    fun getAll(): List<News>

    @Insert
    fun insert(news: News)
}