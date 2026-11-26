package com.sosauce.vanilla.feature.calculator.presentation.components

data class CalcButton(
    val text: String,
    val onClick: () -> Unit,
    val onLongClick: (() -> Unit)? = null,
    val type: ButtonType = ButtonType.OTHER,
    val rectangle: Boolean = false
)
