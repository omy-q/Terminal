package com.example.terminal.presentation

import com.example.terminal.domain.Bar
import com.example.terminal.domain.TimeFrame

sealed interface TerminalScreenState {

    object Initial : TerminalScreenState
    object Loading : TerminalScreenState
    data class Content(
        val barList: List<Bar>,
        val selectedTimeFrame: TimeFrame
    ) : TerminalScreenState

    data class Error(
        val throwable: Throwable,
        val selectedTimeFrame: TimeFrame
    ) :
        TerminalScreenState
}
