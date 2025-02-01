package com.aghajari.composelayoutanimation.transitions

import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.IntSize
import com.aghajari.composelayoutanimation.BaseTransition
import com.aghajari.composelayoutanimation.FiniteAnimationSpecBuilder
import com.aghajari.composelayoutanimation.IntSizeEvaluator
import com.aghajari.composelayoutanimation.LayoutTransition
import com.aghajari.composelayoutanimation.LayoutTransitionImpl
import com.aghajari.composelayoutanimation.LayoutAnimationRunner

@Immutable
private data class Expand(
    val alignment: Alignment?,
    val from: (fullSize: IntSize) -> IntSize,
    val to: (fullSize: IntSize) -> IntSize,
    override val finiteAnimationSpecBuilder: FiniteAnimationSpecBuilder,
) : BaseTransition<IntSize>() {

    override val evaluator = IntSizeEvaluator

    override fun applySize(
        animation: LayoutAnimationRunner<IntSize>,
    ): (IntSize) -> Pair<State<IntSize?>, Alignment?> = { target ->
        animation.animate(
            initialValue = from.invoke(target),
            targetValue = to.invoke(target),
        ) to alignment
    }
}

/**
 * Builder interface for creating size expansion animations.
 * These animations modify actual layout measurements and affect the layout hierarchy.
 */
internal interface ExpandBuilder {

    /**
     * Creates an expansion animation that modifies both width and height.
     * Note: This affects actual layout measurements and can impact surrounding views.
     *
     * Example:
     * ```
     * // Expand from nothing to full size
     * expand()
     * ```
     *
     * @param durationMillis Animation duration
     * @param delayMillis Initial delay before animation starts
     * @param easing Easing curve for the animation
     * @param repeatIterations Number of repetitions (0 = no repeat)
     * @param repeatMode How the animation should repeat
     * @param expandFrom Anchor point for expansion
     * @param clip Whether to clip content during animation
     * @param from Lambda calculating initial size based on full size
     * @param to Lambda calculating target size based on full size
     * @return A [LayoutTransition] configured for the expand transition
     */
    fun expand(
        durationMillis: Int = DefaultDurationMillis,
        delayMillis: Int = 0,
        easing: Easing = FastOutSlowInEasing,
        expandFrom: Alignment? = null,
        repeatIterations: Int = 0,
        repeatMode: RepeatMode = RepeatMode.Restart,
        clip: Boolean = true,
        from: (fullSize: IntSize) -> IntSize = { IntSize.Zero },
        to: (fullSize: IntSize) -> IntSize = { it },
    ): LayoutTransition = LayoutTransitionImpl(
        Expand(
            alignment = expandFrom,
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
        clip = clip,
    )

    /**
     * Creates a horizontal-only expansion animation.
     * Note: This affects actual layout measurements and can impact surrounding views.
     *
     * Example:
     * ```
     * // Expand from left edge
     * expandHorizontally()
     * ```
     *
     * @param durationMillis Animation duration
     * @param delayMillis Initial delay before animation starts
     * @param easing Easing curve for the animation
     * @param repeatIterations Number of repetitions (0 = no repeat)
     * @param repeatMode How the animation should repeat
     * @param expandFrom Anchor point for expansion
     * @param clip Whether to clip content during animation
     * @param from Lambda calculating initial width
     * @param to Lambda calculating target width
     * @return A [LayoutTransition] configured for the expand transition
     */
    fun expandHorizontally(
        durationMillis: Int = DefaultDurationMillis,
        delayMillis: Int = 0,
        easing: Easing = FastOutSlowInEasing,
        expandFrom: Alignment = Alignment.BottomEnd,
        repeatIterations: Int = 0,
        repeatMode: RepeatMode = RepeatMode.Restart,
        clip: Boolean = true,
        from: (fullWidth: Int) -> Int = { 0 },
        to: (fullWidth: Int) -> Int = { it },
    ) = expand(
        durationMillis = durationMillis,
        delayMillis = delayMillis,
        easing = easing,
        repeatIterations = repeatIterations,
        repeatMode = repeatMode,
        expandFrom = expandFrom,
        clip = clip,
        from = { IntSize(from(it.width), it.height) },
        to = { IntSize(to(it.width), it.height) },
    )

    /**
     * Creates a vertical-only expansion animation.
     * Note: This affects actual layout measurements and can impact surrounding views.
     *
     * Example:
     * ```
     * // Expand from top edge
     * expandVertically()
     * ```
     *
     * @param durationMillis Animation duration
     * @param delayMillis Initial delay before animation starts
     * @param easing Easing curve for the animation
     * @param repeatIterations Number of repetitions (0 = no repeat)
     * @param repeatMode How the animation should repeat
     * @param expandFrom Anchor point for expansion
     * @param clip Whether to clip content during animation
     * @param from Lambda calculating initial height
     * @param to Lambda calculating target height
     * @return A [LayoutTransition] configured for the expand transition
     */
    fun expandVertically(
        durationMillis: Int = DefaultDurationMillis,
        delayMillis: Int = 0,
        easing: Easing = FastOutSlowInEasing,
        expandFrom: Alignment = Alignment.BottomEnd,
        repeatIterations: Int = 0,
        repeatMode: RepeatMode = RepeatMode.Restart,
        clip: Boolean = true,
        from: (fullHeight: Int) -> Int = { 0 },
        to: (fullHeight: Int) -> Int = { it },
    ) = expand(
        durationMillis = durationMillis,
        delayMillis = delayMillis,
        easing = easing,
        repeatIterations = repeatIterations,
        repeatMode = repeatMode,
        expandFrom = expandFrom,
        clip = clip,
        from = { IntSize(it.width, from(it.height)) },
        to = { IntSize(it.width, to(it.height)) },
    )
}