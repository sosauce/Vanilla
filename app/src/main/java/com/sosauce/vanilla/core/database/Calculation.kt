package com.sosauce.vanilla.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Calculation(
    val operation: String,
    val result: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)