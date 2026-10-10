package com.sosauce.vanilla.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

// tableName is pinned to the legacy name Room derived from the former
// Calculation @Entity class, so the on-device schema is byte-identical
// and no migration is required (database version stays 1).
@Entity(tableName = "Calculation")
data class CalculationEntity(
    val operation: String,
    val result: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
