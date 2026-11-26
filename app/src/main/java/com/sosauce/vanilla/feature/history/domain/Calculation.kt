package com.sosauce.vanilla.feature.history.domain

data class Calculation(
    val operation: String,
    val result: String,
    val id: Int = 0
)
