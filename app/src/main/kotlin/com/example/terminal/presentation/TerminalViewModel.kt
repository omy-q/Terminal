package com.example.terminal.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.terminal.data.TerminalRepository
import com.example.terminal.data.TerminalRepositoryImpl
import com.example.terminal.domain.TimeFrame
import com.example.terminal.showDebugLog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TerminalViewModel : ViewModel() {

    private val repository: TerminalRepository = TerminalRepositoryImpl()
    private var selectedTimeFrame: TimeFrame = TimeFrame.HOUR

    private val _state = MutableStateFlow<TerminalScreenState>(TerminalScreenState.Initial)
    val state = _state.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        showDebugLog("Exception caught: $throwable")
        _state.value = TerminalScreenState.Error(throwable, selectedTimeFrame)
    }

    init {
        loadBarList()
    }

    fun loadBarList(timeFrame: TimeFrame = TimeFrame.HOUR) {
        selectedTimeFrame = timeFrame
        _state.value = TerminalScreenState.Loading
        viewModelScope.launch(exceptionHandler) {
            val result = repository.loadBars(selectedTimeFrame)
            _state.value = TerminalScreenState.Content(
                barList = result,
                selectedTimeFrame = selectedTimeFrame
            )
        }
    }
}
