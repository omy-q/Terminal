package com.example.terminal.data

import com.example.terminal.domain.Bar

interface TerminalRepository {

    suspend fun loadBars(): List<Bar>
}
