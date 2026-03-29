package com.sosauce.vanilla.ui.screens.calculator.components

data class CalcButton(
    val text: String,
    val onClick: () -> Unit,
    val onLongClick: (() -> Unit)? = null,
    val type: ButtonType = ButtonType.OTHER,
    val rectangle: Boolean = false
)
