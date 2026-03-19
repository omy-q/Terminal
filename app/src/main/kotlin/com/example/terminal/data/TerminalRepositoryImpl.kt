package com.example.terminal.data

import com.example.terminal.data.dto.toDomain
import com.example.terminal.domain.Bar

class TerminalRepositoryImpl: TerminalRepository {

    private val apiService = ApiFactory.apiService

    override suspend fun loadBars(): List<Bar> {
        return apiService.loadBars().toDomain()
    }
}
