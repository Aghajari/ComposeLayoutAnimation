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
private data class Fade(
    override val from: Float,
    override val to: Float,
    override val finiteAnimationSpecBuilder: FiniteAnimationSpecBuilder,
) : BaseGraphicsLayerTransition() {

    override fun GraphicsLayerScope.applyGraphicsLayer(value: Float) {
        this.alpha = value
    }
}

internal interface FadeBuilder {

    /**
     * Creates a fade transition animation.
     *
     * Example:
     * ```
     * // Fade in
     * fade(from = 0f, to = 1f)
     * // Fade out
     * fade(from = 1f, to = 0f)
     * ```
     *
     * @param durationMillis Animation duration
     * @param delayMillis Initial delay before animation starts
     * @param easing Easing curve for the animation
     * @param repeatIterations Number of repetitions (0 = no repeat)
     * @param repeatMode How the animation should repeat
     * @param from Starting alpha value (0f = transparent, 1f = opaque)
     * @param to Target alpha value (0f = transparent, 1f = opaque)
     * @return A [LayoutTransition] configured for the fade transition
     */
    fun fade(
        durationMillis: Int = DefaultDurationMillis,
        delayMillis: Int = 0,
        easing: Easing = FastOutSlowInEasing,
        repeatIterations: Int = 0,
        repeatMode: RepeatMode = RepeatMode.Restart,
        from: Float = 0f,
        to: Float = 1f,
    ): LayoutTransition = LayoutTransitionImpl(
        Fade(
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
}