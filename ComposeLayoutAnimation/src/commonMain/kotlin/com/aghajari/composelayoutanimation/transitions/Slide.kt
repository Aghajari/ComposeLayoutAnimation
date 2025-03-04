package com.aghajari.composelayoutanimation.transitions

import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.aghajari.composelayoutanimation.BaseTransition
import com.aghajari.composelayoutanimation.FiniteAnimationSpecBuilder
import com.aghajari.composelayoutanimation.IntOffsetEvaluator
import com.aghajari.composelayoutanimation.LayoutTransition
import com.aghajari.composelayoutanimation.LayoutTransitionImpl
import com.aghajari.composelayoutanimation.LayoutAnimationRunner

@Immutable
private data class Slide(
    val from: (fullSize: IntSize) -> IntOffset,
    val to: (fullSize: IntSize) -> IntOffset,
    override val finiteAnimationSpecBuilder: FiniteAnimationSpecBuilder,
) : BaseTransition<IntOffset>() {

    override val evaluator = IntOffsetEvaluator

    override fun applyOffset(
        animation: LayoutAnimationRunner<IntOffset>,
    ): (IntSize) -> State<IntOffset?> = { target ->
        animation.animate(
            initialValue = from.invoke(target),
            targetValue = to.invoke(target),
        )
    }
}

/**
 * Builder interface for creating slide animations that modify layout positions.
 * These transitions affect the actual layout structure, unlike GraphicsLayer translations.
 */
internal interface SlideBuilder {

    /**
     * Creates a slide animation that modifies layout position in both directions.
     * Note: This affects actual layout measurements and structure.
     *
     * Example:
     * ```
     * // Slide in from top-left corner
     * slide(
     *     from = { size -> IntOffset(-size.width, -size.height) },
     *     to = { IntOffset.Zero },
     * )
     * ```
     *
     * @param durationMillis Animation duration
     * @param delayMillis Initial delay before animation starts
     * @param easing Easing curve for the animation
     * @param repeatIterations Number of repetitions (0 = no repeat)
     * @param repeatMode How the animation should repeat
     * @param from Lambda calculating initial offset based on view size
     * @param to Lambda calculating target offset based on view size
     * @return A [LayoutTransition] configured for X and Y movement
     */
    fun slide(
        durationMillis: Int = DefaultDurationMillis,
        delayMillis: Int = 0,
        easing: Easing = FastOutSlowInEasing,
        repeatIterations: Int = 0,
        repeatMode: RepeatMode = RepeatMode.Restart,
        from: (fullSize: IntSize) -> IntOffset = { IntOffset(-it.width / 2, -it.height / 2) },
        to: (fullSize: IntSize) -> IntOffset = { IntOffset.Zero },
    ): LayoutTransition = LayoutTransitionImpl(
        Slide(
            from = from,
            to = to,
            finiteAnimationSpecBuilder = FiniteAnimationSpecBuilder(
                durationMillis = durationMillis,
                delayMillis = delayMillis,
                easing = easing,
                repeatIterations = repeatIterations,
                repeatMode = repeatMode,
            ),
        ),
    )

    /**
     * Creates a vertical slide animation that modifies layout position.
     * Note: This affects actual layout measurements and structure.
     *
     * Example:
     * ```
     * // Slide in from above
     * slideVertically(
     *     from = { height -> -height },  // Start fully above
     *     to = { 0 },                    // End at original position
     * )
     * ```
     *
     * @param durationMillis Animation duration
     * @param delayMillis Initial delay before animation starts
     * @param easing Easing curve for the animation
     * @param repeatIterations Number of repetitions (0 = no repeat)
     * @param repeatMode How the animation should repeat
     * @param from Lambda calculating initial vertical offset based on height
     * @param to Lambda calculating target vertical offset based on height
     * @return A [LayoutTransition] configured for vertical movement
     */
    fun slideVertically(
        durationMillis: Int = DefaultDurationMillis,
        delayMillis: Int = 0,
        easing: Easing = FastOutSlowInEasing,
        repeatIterations: Int = 0,
        repeatMode: RepeatMode = RepeatMode.Restart,
        from: (fullHeight: Int) -> Int = { -it / 2 },
        to: (fullHeight: Int) -> Int = { 0 },
    ) = slide(
        durationMillis = durationMillis,
        delayMillis = delayMillis,
        easing = easing,
        repeatIterations = repeatIterations,
        repeatMode = repeatMode,
        from = { IntOffset(0, from(it.height)) },
        to = { IntOffset(0, to(it.height)) },
    )

    /**
     * Creates a horizontal slide animation that modifies layout position.
     * Note: This affects actual layout measurements and structure.
     *
     * Example:
     * ```
     * // Slide in from left
     * slideHorizontally(
     *     from = { width -> -width },  // Start fully to the left
     *     to = { 0 },                  // End at original position
     * )
     * ```
     *
     * @param durationMillis Animation duration
     * @param delayMillis Initial delay before animation starts
     * @param easing Easing curve for the animation
     * @param repeatIterations Number of repetitions (0 = no repeat)
     * @param repeatMode How the animation should repeat
     * @param from Lambda calculating initial horizontal offset based on width
     * @param to Lambda calculating target horizontal offset based on width
     * @return A [LayoutTransition] configured for horizontal movement
     */
    fun slideHorizontally(
        durationMillis: Int = DefaultDurationMillis,
        delayMillis: Int = 0,
        easing: Easing = FastOutSlowInEasing,
        repeatIterations: Int = 0,
        repeatMode: RepeatMode = RepeatMode.Restart,
        from: (fullWidth: Int) -> Int = { -it / 2 },
        to: (fullWidth: Int) -> Int = { 0 },
    ) = slide(
        durationMillis = durationMillis,
        delayMillis = delayMillis,
        easing = easing,
        repeatIterations = repeatIterations,
        repeatMode = repeatMode,
        from = { IntOffset(from(it.width), 0) },
        to = { IntOffset(to(it.width), 0) },
    )
}