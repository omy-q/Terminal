package com.example.terminal.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import com.example.terminal.domain.Bar
import kotlin.math.roundToInt

private const val MIN_VISIBLE_BARS_COUNT = 20

@Composable
fun Terminal(
    bars: List<Bar>,
    modifier: Modifier = Modifier
) {
    var terminalState by rememberTerminalState(bars)
    val transformableState = rememberTransformableState { zoomChange, panChange, _ ->
        val visibleBarsCount = (terminalState.visibleBarsCount / zoomChange)
            .roundToInt()
            .coerceIn(MIN_VISIBLE_BARS_COUNT, bars.size)
        val scrolledBy = (terminalState.scrolledBy + panChange.x)
            .coerceIn(0f, (bars.size * terminalState.barWidth - terminalState.terminalWidth))

        terminalState = terminalState.copy(
            visibleBarsCount = visibleBarsCount,
            scrolledBy = scrolledBy
        )
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .transformable(transformableState)
            .onSizeChanged {
                terminalState = terminalState.copy(terminalWidth = it.width.toFloat())
            }
    ) {
        val max = terminalState.visibleBars.maxOf { it.high }
        val min = terminalState.visibleBars.minOf { it.low }
        val pxPerPoint = size.height / (max - min)
        translate(left = terminalState.scrolledBy) {
            bars.forEachIndexed { index, bar ->
                drawLine(
                    color = Color.White,
                    start = Offset(
                        x = (size.width - index * terminalState.barWidth),
                        y = (size.height - (bar.low - min) * pxPerPoint)
                    ),
                    end = Offset(
                        x = (size.width - index * terminalState.barWidth),
                        y = (size.height - (bar.high - min) * pxPerPoint)
                    ),
                    strokeWidth = 1f
                )

                drawLine(
                    color = if (bar.open < bar.close) Color.Green else Color.Red,
                    start = Offset(
                        x = (size.width - index * terminalState.barWidth),
                        y = (size.height - (bar.open - min) * pxPerPoint)
                    ),
                    end = Offset(
                        x = (size.width - index * terminalState.barWidth),
                        y = (size.height - (bar.close - min) * pxPerPoint)
                    ),
                    strokeWidth = terminalState.barWidth / 2
                )
            }
        }
    }
}
