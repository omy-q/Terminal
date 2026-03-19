package com.example.terminal.data.dto

import com.example.terminal.domain.Bar
import com.google.gson.annotations.SerializedName

data class BarsResponse(
    @SerializedName("results") val barList: List<BarResponse>
)

fun BarsResponse.toDomain(): List<Bar> {
    return barList.map { it.toDomain() }
}
