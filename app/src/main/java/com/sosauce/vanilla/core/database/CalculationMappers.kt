package com.sosauce.vanilla.core.database

import com.sosauce.vanilla.feature.history.domain.Calculation

fun CalculationEntity.toCalculation(): Calculation = Calculation(
    operation = operation,
    result = result,
    id = id
)

fun Calculation.toCalculationEntity(): CalculationEntity = CalculationEntity(
    operation = operation,
    result = result,
    id = id
)
