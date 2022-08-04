package com.bottomnavcomp.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class News(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val createdAt: Long
) : Serializable {
    @Ignore
    constructor() : this(0, "", 0)
}
