package com.example.terminal.presentation.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.terminal.domain.Bar
import com.example.terminal.domain.TimeFrame
import java.util.Calendar
import java.util.Locale

fun DrawScope.drawTimeDelimiter(
    timeFrame: TimeFrame,
    bar: Bar,
    nextBar: Bar?,
    offsetX: Float,
    textMeasurer: TextMeasurer
) {
    val calendar = bar.calendar
    val minutes = calendar.get(Calendar.MINUTE)
    val hours = calendar.get(Calendar.HOUR_OF_DAY)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val shouldDrawDelimiter = when (timeFrame) {
        TimeFrame.MIN_5 -> minutes == 0
        TimeFrame.MIN_15 -> minutes == 0 && hours % 2 == 0
        TimeFrame.MIN_30, TimeFrame.HOUR -> {
            val nextBarDay = nextBar?.calendar?.get(Calendar.DAY_OF_MONTH)
            day != nextBarDay
        }
    }
    if (!shouldDrawDelimiter) return

    drawLine(
        color = Color.White.copy(alpha = 0.5f),
        start = Offset(x = offsetX, y = 0f),
        end = Offset(x = offsetX, y = size.height),
        strokeWidth = 1f,
        pathEffect = PathEffect.dashPathEffect(intervals = floatArrayOf(4.dp.toPx(), 4.dp.toPx()))
    )

    val text = when (timeFrame) {
        TimeFrame.MIN_5, TimeFrame.MIN_15 -> {
            String.format(Locale.getDefault(), "%02d:00", hours)
        }

        TimeFrame.MIN_30, TimeFrame.HOUR -> {
            val nameOfMonth = calendar.getDisplayName(
                Calendar.MONTH,
                Calendar.SHORT,
                Locale.getDefault()
            )
            String.format(Locale.getDefault(), "%s %s", day, nameOfMonth)
        }
    }
    val textLayoutResult = textMeasurer.measure(
        text = text,
        style = TextStyle(color = Color.White, fontSize = 12.sp)
    )
    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(
            x = offsetX - textLayoutResult.size.width / 2,
            y = size.height
        )
    )
}
