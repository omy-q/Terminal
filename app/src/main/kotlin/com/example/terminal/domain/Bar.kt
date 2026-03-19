package com.example.terminal.domain

import androidx.compose.runtime.Immutable
import java.util.Calendar

@Immutable
data class Bar(
    val open: Float,
    val close: Float,
    val low: Float,
    val high: Float,
    val time: Long,
    val calendar: Calendar
)
