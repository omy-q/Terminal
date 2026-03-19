package com.example.terminal.presentation

import com.example.terminal.domain.Bar

sealed interface TerminalScreenState {

    object Initial : TerminalScreenState
    data class Content(val barList: List<Bar>) : TerminalScreenState
}
