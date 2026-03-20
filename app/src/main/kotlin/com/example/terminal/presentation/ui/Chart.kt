package com.example.terminal.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.example.terminal.domain.TimeFrame
import com.example.terminal.presentation.TerminalState
import kotlin.math.roundToInt

private const val MIN_VISIBLE_BARS_COUNT = 20

@Composable
fun Chart(
    terminalState: State<TerminalState>,
    onTerminalStateChanged: (TerminalState) -> Unit,
    timeFrame: TimeFrame,
    modifier: Modifier = Modifier
) {
    val currentState = terminalState.value
    val textMeasurer = rememberTextMeasurer()
    val transformableState = rememberTransformableState { zoomChange, panChange, _ ->
        val visibleBarsCount = (currentState.visibleBarsCount / zoomChange)
            .roundToInt()
            .coerceIn(MIN_VISIBLE_BARS_COUNT, currentState.barList.size)
        val scrolledBy = (currentState.scrolledBy + panChange.x)
            .coerceIn(
                0f,
                currentState.barList.size * currentState.barWidth - currentState.terminalWidth
            )

        onTerminalStateChanged(
            currentState.copy(
                visibleBarsCount = visibleBarsCount,
                scrolledBy = scrolledBy
            )
        )
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .clipToBounds()
            .padding(top = 32.dp, bottom = 32.dp, end = 32.dp)
            .transformable(transformableState)
            .onSizeChanged {
                onTerminalStateChanged(
                    currentState.copy(
                        terminalWidth = it.width.toFloat(),
                        terminalHeight = it.height.toFloat()
                    )
                )
            }
    ) {
        val min = currentState.min
        val pxPerPoint = currentState.pxPerPoint
        translate(left = currentState.scrolledBy) {
            currentState.barList.forEachIndexed { index, bar ->
                val offsetX = size.width - index * currentState.barWidth
                drawTimeDelimiter(
                    timeFrame = timeFrame,
                    bar = bar,
                    nextBar = currentState.barList.getOrNull(index + 1),
                    offsetX = offsetX,
                    textMeasurer = textMeasurer
                )
                drawLine(
                    color = Color.White,
                    start = Offset(
                        x = offsetX,
                        y = size.height - ((bar.low - min) * pxPerPoint)
                    ),
                    end = Offset(
                        x = offsetX,
                        y = size.height - ((bar.high - min) * pxPerPoint)
                    ),
                    strokeWidth = 1f
                )
                drawLine(
                    color = if (bar.open < bar.close) Color.Green else Color.Red,
                    start = Offset(
                        x = offsetX,
                        y = size.height - ((bar.open - min) * pxPerPoint)
                    ),
                    end = Offset(
                        x = offsetX,
                        y = size.height - ((bar.close - min) * pxPerPoint)
                    ),
                    strokeWidth = currentState.barWidth / 2
                )
            }
        }
    }
}
