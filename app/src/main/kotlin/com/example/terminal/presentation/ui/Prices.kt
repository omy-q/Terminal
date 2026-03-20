package com.example.terminal.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.terminal.presentation.TerminalState

@Composable
fun Prices(
    terminalState: State<TerminalState>,
    lastPrice: Float,
    modifier: Modifier = Modifier
) {
    val currentState = terminalState.value
    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .clipToBounds()
            .padding(vertical = 32.dp)
    ) {
        drawPrices(
            max = currentState.max,
            min = currentState.min,
            lastPrice = lastPrice,
            pxPerPoint = currentState.pxPerPoint,
            textMeasurer = textMeasurer
        )
    }
}

private fun DrawScope.drawPrices(
    max: Float,
    min: Float,
    lastPrice: Float,
    pxPerPoint: Float,
    textMeasurer: TextMeasurer
) {
    // max
    val maxPriceOffsetY = 0f
    drawDashedLine(
        start = Offset(x = 0f, y = maxPriceOffsetY),
        end = Offset(x = size.width, y = maxPriceOffsetY)
    )
    drawTextPrice(
        textMeasurer = textMeasurer,
        price = max,
        offsetY = maxPriceOffsetY
    )

    // lastPrice
    val lastPriceOffsetY = (size.height - (lastPrice - min) * pxPerPoint)
    drawDashedLine(
        start = Offset(x = 0f, y = lastPriceOffsetY),
        end = Offset(x = size.width, y = lastPriceOffsetY)
    )
    drawTextPrice(
        textMeasurer = textMeasurer,
        price = lastPrice,
        offsetY = lastPriceOffsetY
    )

    // min
    val minPriceOffsetY = size.height
    drawDashedLine(
        start = Offset(x = 0f, y = minPriceOffsetY),
        end = Offset(x = size.width, y = minPriceOffsetY)
    )
    drawTextPrice(
        textMeasurer = textMeasurer,
        price = min,
        offsetY = minPriceOffsetY
    )

}

private fun DrawScope.drawDashedLine(
    start: Offset,
    end: Offset,
    color: Color = Color.White,
    strokeWidth: Float = 1f
) {
    drawLine(
        color = color,
        start = start,
        end = end,
        strokeWidth = strokeWidth,
        pathEffect = PathEffect.dashPathEffect(intervals = floatArrayOf(4.dp.toPx(), 4.dp.toPx()))
    )
}

private fun DrawScope.drawTextPrice(
    textMeasurer: TextMeasurer,
    price: Float,
    offsetY: Float
) {
    val textLayoutResult = textMeasurer.measure(
        text = price.toString(),
        style = TextStyle(color = Color.White, fontSize = 12.sp)
    )
    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(
            x = (size.width - (textLayoutResult.size.width + 8.dp.toPx())),
            y = offsetY
        )
    )
}
