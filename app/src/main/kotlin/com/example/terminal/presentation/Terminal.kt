package com.example.terminal.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.terminal.domain.Bar

@Composable
fun Terminal(
    bars: List<Bar>,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val max = bars.maxOf { it.high }
        val min = bars.minOf { it.low }
        val barWidth = size.width / bars.size
        val pxPerPoint = size.height / (max - min)
        bars.forEachIndexed { index, bar ->
            drawLine(
                color = Color.White,
                start = Offset(
                    x = index * barWidth,
                    y = (size.height - (bar.low - min) * pxPerPoint)
                ),
                end = Offset(
                    x = index * barWidth,
                    y = (size.height - (bar.high - min) * pxPerPoint)
                ),
                strokeWidth = 1f
            )
        }
    }
}
