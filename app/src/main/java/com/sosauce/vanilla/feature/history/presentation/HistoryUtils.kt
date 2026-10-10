package com.sosauce.vanilla.feature.history.presentation

import com.sosauce.vanilla.feature.history.domain.Calculation

fun List<Calculation>.sort(
    newestFirst: Boolean
): List<Calculation> {
    return if (newestFirst) {
        this.sortedByDescending { it.id }
    } else {
        this
    }
}
