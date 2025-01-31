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
private data class Custom(
    override val finiteAnimationSpecBuilder: FiniteAnimationSpecBuilder,
    val block: GraphicsLayerScope.(fraction: Float) -> Unit,
) : BaseGraphicsLayerTransition() {

    /**
     * Animation always runs from 0 to 1, representing the progress fraction.
     * The actual values for transformations are determined in the custom block.
     */
    override val from: Float = 0f
    override val to: Float = 1f

    override fun GraphicsLayerScope.applyGraphicsLayer(value: Float) {
        block.invoke(this, value)
    }
}

/**
 * Builder interface for creating custom GraphicsLayer animations.
 * Allows direct manipulation of any GraphicsLayer properties based on animation progress.
 */
internal interface CustomBuilder {

    /**
     * Creates a custom animation with direct access to GraphicsLayer properties.
     * Provides complete control over visual transformations through a custom block.
     *
     * The animation progress is provided as a float from 0.0 to 1.0, where:
     * - 0.0 represents the start of the animation
     * - 1.0 represents the end of the animation
     *
     * Example usages:
     * ```
     * // Combined scale and rotation
     * custom { fraction ->
     *     scale = 1f + fraction * 0.5f     // Scale from 100% to 150%
     *     rotationZ = fraction * 360f      // Rotate full circle
     * }
     *
     * // Complex transformation
     * custom { fraction ->
     *     alpha = fraction                 // Fade in
     *     scaleX = 1f + fraction           // Horizontal scale
     *     rotationY = fraction * 45f       // Slight Y rotation
     *     translationX = fraction * 100f   // Move right
     * }
     *
     * // Spring-like effect
     * custom { fraction ->
     *     val bounce = sin(fraction * Math.PI * 4).toFloat()
     *     translationY = bounce * 20f
     * }
     * ```
     *
     * @param durationMillis Duration of the animation
     * @param delayMillis Initial delay before animation starts
     * @param easing Easing curve for the animation
     * @param repeatIterations Number of times to repeat (0 = no repeat)
     * @param repeatMode How the animation should repeat
     * @param block Lambda with animation fraction and GraphicsLayer scope
     * @return A [LayoutTransition] configured with the custom transformation
     */
    fun custom(
        durationMillis: Int = DefaultDurationMillis,
        delayMillis: Int = 0,
        easing: Easing = FastOutSlowInEasing,
        repeatIterations: Int = 0,
        repeatMode: RepeatMode = RepeatMode.Restart,
        block: GraphicsLayerScope.(fraction: Float) -> Unit,
    ): LayoutTransition = LayoutTransitionImpl(
        Custom(
            finiteAnimationSpecBuilder = FiniteAnimationSpecBuilder(
                durationMillis = durationMillis,
                delayMillis = delayMillis,
                easing = easing,
                repeatIterations = repeatIterations,
                repeatMode = repeatMode,
            ),
            block = block,
        ),
    )
}