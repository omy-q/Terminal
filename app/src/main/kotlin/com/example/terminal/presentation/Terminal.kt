package com.example.terminal.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.terminal.presentation.ui.Chart
import com.example.terminal.presentation.ui.Prices
import com.example.terminal.presentation.ui.TimeFrames
import com.example.terminal.showDebugLog


@Composable
fun Terminal(
    modifier: Modifier = Modifier
) {
    val viewModel: TerminalViewModel = viewModel()
    val screenState = viewModel.state.collectAsState()
    when (val currentState = screenState.value) {
        is TerminalScreenState.Content -> {
            showDebugLog("Content bars = ${currentState.barList}")
            val terminalState = rememberTerminalState(currentState.barList)
            Box(modifier = modifier)
            {
                Chart(
                    terminalState = terminalState,
                    onTerminalStateChanged = { terminalState.value = it },
                    timeFrame = currentState.selectedTimeFrame
                )

                currentState.barList.firstOrNull()?.let {
                    Prices(
                        terminalState = terminalState,
                        lastPrice = it.close
                    )
                }

                TimeFrames(
                    selectedTimeFrame = currentState.selectedTimeFrame,
                    onTimeFrameSelected = viewModel::loadBarList
                )
            }

        }

        is TerminalScreenState.Error -> {
            showDebugLog("Error")
            Box(modifier = modifier.fillMaxSize()) {
                Text(
                    text = currentState.throwable.toString(),
                    modifier = Modifier.align(Alignment.Center)
                )
                Button(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp),
                    onClick = {
                        viewModel.loadBarList(currentState.selectedTimeFrame)
                    }
                ) {
                    Text("Repeat")
                }
            }
        }

        TerminalScreenState.Loading -> {
            showDebugLog("Loading")
            Box(modifier = modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        TerminalScreenState.Initial -> {
            showDebugLog("Initial")
        }
    }
}
