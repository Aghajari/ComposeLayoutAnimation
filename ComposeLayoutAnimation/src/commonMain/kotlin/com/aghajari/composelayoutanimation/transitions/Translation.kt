package com.aghajari.composelayoutanimation.transitions

import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.GraphicsLayerScope
import com.aghajari.composelayoutanimation.BaseGraphicsLayerTransition
import com.aghajari.composelayoutanimation.FiniteAnimationSpecBuilder
import com.aghajari.composelayoutanimation.LayoutTransition
import com.aghajari.composelayoutanimation.LayoutTransitionImpl

@Immutable
private data class TranslationX(
    override val from: Float,
    override val to: Float,
    override val finiteAnimationSpecBuilder: FiniteAnimationSpecBuilder,
) : BaseGraphicsLayerTransition() {

    override fun GraphicsLayerScope.applyGraphicsLayer(value: Float) {
        this.translationX = value
    }
}

@Immutable
private data class TranslationY(
    override val from: Float,
    override val to: Float,
    override val finiteAnimationSpecBuilder: FiniteAnimationSpecBuilder,
) : BaseGraphicsLayerTransition() {

    override fun GraphicsLayerScope.applyGraphicsLayer(value: Float) {
        this.translationY = value
    }
}

/**
 * Builder interface for creating translation animations.
 * All translations are performed using GraphicsLayer modifications, which means:
 * - No impact on layout measurements
 * - No effect on surrounding views' positions
 * - Pure visual transformation without layout changes
 */
internal interface TranslationBuilder {

    /**
     * Creates a horizontal translation animation using GraphicsLayer.
     * Moves the view horizontally without affecting layout measurements.
     *
     * Example:
     * ```
     * // Slide in from 100px right
     * translationX(from = 100f, to = 0f)
     *
     * // Slide out to left
     * translationX(from = 0f, to = -100f)
     * ```
     *
     * @param durationMillis Animation duration
     * @param delayMillis Initial delay before animation starts
     * @param easing Easing curve for the animation
     * @param repeatIterations Number of repetitions (0 = no repeat)
     * @param repeatMode How the animation should repeat
     * @param from Starting X translation in pixels
     * @param to Target X translation in pixels
     * @return A [LayoutTransition] configured for horizontal translation
     */
    fun translationX(
        durationMillis: Int = DefaultDurationMillis,
        delayMillis: Int = 0,
        easing: Easing = FastOutSlowInEasing,
        repeatIterations: Int = 0,
        repeatMode: RepeatMode = RepeatMode.Restart,
        from: Float = 100f,
        to: Float = 0f,
    ): LayoutTransition = LayoutTransitionImpl(
        TranslationX(
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
     * Creates a vertical translation animation using GraphicsLayer.
     * Moves the view vertically without affecting layout measurements.
     *
     * Example:
     * ```
     * // Slide in from 100px below
     * translationY(from = 100f, to = 0f)
     *
     * // Slide out upward
     * translationY(from = 0f, to = -100f)
     * ```
     *
     * @param durationMillis Animation duration
     * @param delayMillis Initial delay before animation starts
     * @param easing Easing curve for the animation
     * @param repeatIterations Number of repetitions (0 = no repeat)
     * @param repeatMode How the animation should repeat
     * @param from Starting Y translation in pixels
     * @param to Target Y translation in pixels
     * @return A [LayoutTransition] configured for vertical translation
     */
    fun translationY(
        durationMillis: Int = DefaultDurationMillis,
        delayMillis: Int = 0,
        easing: Easing = FastOutSlowInEasing,
        repeatIterations: Int = 0,
        repeatMode: RepeatMode = RepeatMode.Restart,
        from: Float = 100f,
        to: Float = 0f,
    ): LayoutTransition = LayoutTransitionImpl(
        TranslationY(
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
     * Creates a combined X and Y translation animation using GraphicsLayer.
     * Moves the view in both directions simultaneously without affecting layout.
     *
     * Note: This creates two separate translation animations that run together,
     * allowing independent control of X and Y movements.
     *
     * Example:
     * ```
     * // Diagonal slide in from bottom-right
     * translation(
     *     fromX = 100f,
     *     toX = 0f,
     *     fromY = 100f,
     *     toY = 0f,
     * )
     * ```
     *
     * @param durationMillis Animation duration
     * @param delayMillis Initial delay before animation starts
     * @param easing Easing curve for the animation
     * @param repeatIterations Number of repetitions (0 = no repeat)
     * @param repeatMode How the animation should repeat
     * @param fromX Starting X translation in pixels
     * @param toX Target X translation in pixels
     * @param fromY Starting Y translation in pixels
     * @param toY Target Y translation in pixels
     * @return A combined [LayoutTransition] for X and Y translations
     */
    fun translation(
        durationMillis: Int = DefaultDurationMillis,
        delayMillis: Int = 0,
        easing: Easing = FastOutSlowInEasing,
        repeatIterations: Int = 0,
        repeatMode: RepeatMode = RepeatMode.Restart,
        fromX: Float = 100f,
        toX: Float = 0f,
        fromY: Float = 100f,
        toY: Float = 0f,
    ) = translationX(
        durationMillis = durationMillis,
        delayMillis = delayMillis,
        easing = easing,
        repeatIterations = repeatIterations,
        repeatMode = repeatMode,
        from = fromX,
        to = toX,
    ) + translationX(
        durationMillis = durationMillis,
        delayMillis = delayMillis,
        easing = easing,
        repeatIterations = repeatIterations,
        repeatMode = repeatMode,
        from = fromY,
        to = toY,
    )
}