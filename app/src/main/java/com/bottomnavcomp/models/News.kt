package com.bottomnavcomp.models

import java.io.Serializable

data class News(
    val title: String,
    val createdAt: Long
) : Serializable
