package com.example.terminal.data

import com.example.terminal.data.dto.toDomain
import com.example.terminal.domain.Bar
import com.example.terminal.domain.TimeFrame

class TerminalRepositoryImpl : TerminalRepository {

    private val apiService = ApiFactory.apiService

    override suspend fun loadBars(timeFrame: TimeFrame): List<Bar> {
        val (multiplier, timespan) = timeFrame.getTimeFrameForRequest()
        return apiService.loadBars(
            multiplier = multiplier,
            timespan = timespan,
        ).toDomain()
    }

    private fun TimeFrame.getTimeFrameForRequest(): Pair<Int, String> {
        return when (this) {
            TimeFrame.MIN_5 -> 5 to "minute"
            TimeFrame.MIN_15 -> 15 to "minute"
            TimeFrame.MIN_30 -> 30 to "minute"
            TimeFrame.HOUR -> 1 to "hour"
        }
    }
}
