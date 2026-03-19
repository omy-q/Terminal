package com.example.terminal.data.dto

import com.example.terminal.domain.Bar
import com.google.gson.annotations.SerializedName
import java.util.Calendar
import java.util.Date

data class BarResponse(
    @SerializedName("o") val open: Float,
    @SerializedName("c") val close: Float,
    @SerializedName("l") val low: Float,
    @SerializedName("h") val high: Float,
    @SerializedName("t") val time: Long
)

fun BarResponse.toDomain(): Bar {
    return Bar(
        open = open,
        close = close,
        low = low,
        high = high,
        time = time,
        calendar = Calendar.getInstance().apply {
            time = Date(this@toDomain.time)
        }
    )
}
