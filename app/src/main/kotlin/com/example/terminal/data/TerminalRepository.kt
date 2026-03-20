package com.example.terminal.data

import com.example.terminal.domain.Bar
import com.example.terminal.domain.TimeFrame

interface TerminalRepository {

    suspend fun loadBars(timeFrame: TimeFrame): List<Bar>
}
