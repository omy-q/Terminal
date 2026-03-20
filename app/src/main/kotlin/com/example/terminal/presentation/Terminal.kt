package com.example.terminal.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import com.example.terminal.domain.Bar
import kotlin.math.roundToInt

private const val MIN_VISIBLE_BARS_COUNT = 20

@Composable
fun Terminal(
    bars: List<Bar>,
    modifier: Modifier = Modifier
) {
    var terminalWidth by remember { mutableFloatStateOf(0f) }
    var barWidth by remember { mutableFloatStateOf(0f) }
    var visibleBarsCount by remember { mutableIntStateOf(100) }
    var scrolledBy by remember { mutableFloatStateOf(0f) }
    val transformableState = rememberTransformableState { zoomChange, panChange, _ ->
        visibleBarsCount = (visibleBarsCount / zoomChange)
            .roundToInt()
            .coerceIn(MIN_VISIBLE_BARS_COUNT, bars.size)
        scrolledBy = (scrolledBy + panChange.x)
            .coerceIn(0f, (bars.size * barWidth - terminalWidth))

    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .transformable(transformableState)
    ) {
        terminalWidth = size.width
        val max = bars.maxOf { it.high }
        val min = bars.minOf { it.low }
        barWidth = size.width / visibleBarsCount
        val pxPerPoint = size.height / (max - min)
        translate(left = scrolledBy) {
            bars.forEachIndexed { index, bar ->
                drawLine(
                    color = Color.White,
                    start = Offset(
                        x = (size.width - index * barWidth),
                        y = (size.height - (bar.low - min) * pxPerPoint)
                    ),
                    end = Offset(
                        x = (size.width - index * barWidth),
                        y = (size.height - (bar.high - min) * pxPerPoint)
                    ),
                    strokeWidth = 1f
                )

                drawLine(
                    color = if (bar.open < bar.close) Color.Green else Color.Red,
                    start = Offset(
                        x = (size.width - index * barWidth),
                        y = (size.height - (bar.open - min) * pxPerPoint)
                    ),
                    end = Offset(
                        x = (size.width - index * barWidth),
                        y = (size.height - (bar.close - min) * pxPerPoint)
                    ),
                    strokeWidth = barWidth / 2
                )
            }
        }
    }
}
