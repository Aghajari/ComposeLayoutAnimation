package com.aghajari.composelayoutanimation

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

internal fun interface Evaluator<T> {
    fun evaluate(start: T, stop: T, fraction: Float): T
}

internal val FloatEvaluator = Evaluator<Float> { start, stop, fraction ->
    lerp(start, stop, fraction)
}

internal val IntSizeEvaluator = Evaluator<IntSize> { start, stop, fraction ->
    IntSize(
        width = lerp(start.width, stop.width, fraction),
        height = lerp(start.height, stop.height, fraction),
    )
}

internal val IntOffsetEvaluator = Evaluator<IntOffset> { start, stop, fraction ->
    IntOffset(
        x = lerp(start.x, stop.x, fraction),
        y = lerp(start.y, stop.y, fraction),
    )
}

private fun lerp(start: Float, stop: Float, fraction: Float) =
    (start * (1 - fraction) + stop * fraction)

private fun lerp(start: Int, stop: Int, fraction: Float) =
    (start * (1 - fraction) + stop * fraction).toInt()
