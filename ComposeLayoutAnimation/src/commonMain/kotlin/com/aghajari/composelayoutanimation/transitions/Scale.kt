package com.aghajari.composelayoutanimation.transitions

import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.TransformOrigin
import com.aghajari.composelayoutanimation.BaseGraphicsLayerTransition
import com.aghajari.composelayoutanimation.FiniteAnimationSpecBuilder
import com.aghajari.composelayoutanimation.LayoutTransition
import com.aghajari.composelayoutanimation.LayoutTransitionImpl

@Immutable
private data class Scale(
    override val from: Float,
    override val to: Float,
    val transformOrigin: TransformOrigin?,
    override val finiteAnimationSpecBuilder: FiniteAnimationSpecBuilder,
) : BaseGraphicsLayerTransition() {

    override fun GraphicsLayerScope.applyGraphicsLayer(value: Float) {
        this.scaleX = value
        this.scaleY = value
        this@Scale.transformOrigin?.let {
            this.transformOrigin = it
        }
    }
}

@Immutable
private data class ScaleX(
    override val from: Float,
    override val to: Float,
    val transformOrigin: TransformOrigin?,
    override val finiteAnimationSpecBuilder: FiniteAnimationSpecBuilder,
) : BaseGraphicsLayerTransition() {

    override fun GraphicsLayerScope.applyGraphicsLayer(value: Float) {
        this.scaleX = value
        this@ScaleX.transformOrigin?.let {
            this.transformOrigin = it
        }
    }
}

@Immutable
private data class ScaleY(
    override val from: Float,
    override val to: Float,
    val transformOrigin: TransformOrigin?,
    override val finiteAnimationSpecBuilder: FiniteAnimationSpecBuilder,
) : BaseGraphicsLayerTransition() {

    override fun GraphicsLayerScope.applyGraphicsLayer(value: Float) {
        this.scaleY = value
        this@ScaleY.transformOrigin?.let {
            this.transformOrigin = it
        }
    }
}

/**
 * Builder interface for creating scale animations.
 * Provides methods for scaling in X-axis, Y-axis, or both uniformly.
 */
internal interface ScaleBuilder {

    /**
     * Creates a horizontal scale animation.
     *
     * @param durationMillis Animation duration
     * @param delayMillis Initial delay before animation starts
     * @param easing Easing curve for the animation
     * @param repeatIterations Number of repetitions (0 = no repeat)
     * @param repeatMode How the animation should repeat
     * @param from Starting scale factor for width
     * @param to Target scale factor for width
     * @param transformOrigin Point of origin for scaling (null = center)
     * @return A [LayoutTransition] configured for horizontal scaling
     */
    fun scaleX(
        durationMillis: Int = DefaultDurationMillis,
        delayMillis: Int = 0,
        easing: Easing = FastOutSlowInEasing,
        repeatIterations: Int = 0,
        repeatMode: RepeatMode = RepeatMode.Restart,
        from: Float = 0f,
        to: Float = 1f,
        transformOrigin: TransformOrigin? = null,
    ): LayoutTransition = LayoutTransitionImpl(
        ScaleX(
            from = from,
            to = to,
            transformOrigin = transformOrigin,
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
     * Creates a vertical scale animation.
     *
     * @param durationMillis Animation duration
     * @param delayMillis Initial delay before animation starts
     * @param easing Easing curve for the animation
     * @param repeatIterations Number of repetitions (0 = no repeat)
     * @param repeatMode How the animation should repeat
     * @param from Starting scale factor for height
     * @param to Target scale factor for height
     * @param transformOrigin Point of origin for scaling (null = center)
     * @return A [LayoutTransition] configured for vertical scaling
     */
    fun scaleY(
        durationMillis: Int = DefaultDurationMillis,
        delayMillis: Int = 0,
        easing: Easing = FastOutSlowInEasing,
        repeatIterations: Int = 0,
        repeatMode: RepeatMode = RepeatMode.Restart,
        from: Float = 0f,
        to: Float = 1f,
        transformOrigin: TransformOrigin? = null,
    ): LayoutTransition = LayoutTransitionImpl(
        ScaleY(
            from = from,
            to = to,
            transformOrigin = transformOrigin,
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
     * Creates a uniform scale animation (both axes scale equally).
     *
     * Example:
     * ```
     * // Scale up from 50% to 100%
     * scale(from = 0.5f, to = 1f)
     *
     * // Scale from corner
     * scale(
     *     from = 0f,
     *     to = 1f,
     *     transformOrigin = TransformOrigin(0f, 0f),
     * )
     * ```
     *
     * @param durationMillis Animation duration
     * @param delayMillis Initial delay before animation starts
     * @param easing Easing curve for the animation
     * @param repeatIterations Number of repetitions (0 = no repeat)
     * @param repeatMode How the animation should repeat
     * @param from Starting scale factor for both width and height
     * @param to Target scale factor for both width and height
     * @param transformOrigin Point of origin for scaling (null = center)
     * @return A [LayoutTransition] configured for uniform scaling
     */
    fun scale(
        durationMillis: Int = DefaultDurationMillis,
        delayMillis: Int = 0,
        easing: Easing = FastOutSlowInEasing,
        repeatIterations: Int = 0,
        repeatMode: RepeatMode = RepeatMode.Restart,
        from: Float = 0f,
        to: Float = 1f,
        transformOrigin: TransformOrigin? = null,
    ): LayoutTransition = LayoutTransitionImpl(
        Scale(
            from = from,
            to = to,
            transformOrigin = transformOrigin,
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
     * Creates a scale animation with different X and Y scale factors.
     *
     * If fromX == fromY and toX == toY, creates a uniform scale animation.
     * Otherwise, combines separate X and Y scale animations to run together.
     *
     * Example:
     * ```
     * // Scale width and height differently
     * scale(
     *     fromX = 0.5f,
     *     toX = 1f,
     *     fromY = 0.8f,
     *     toY = 1.2f,
     * )
     * ```
     *
     * @param durationMillis Animation duration
     * @param delayMillis Initial delay before animation starts
     * @param easing Easing curve for the animation
     * @param repeatIterations Number of repetitions (0 = no repeat)
     * @param repeatMode How the animation should repeat
     * @param fromX Starting scale factor for width
     * @param toX Target scale factor for width
     * @param fromY Starting scale factor for height
     * @param toY Target scale factor for height
     * @param transformOrigin Point of origin for scaling (null = center)
     * @return A [LayoutTransition] configured for non-uniform scaling
     */
    fun scale(
        durationMillis: Int = DefaultDurationMillis,
        delayMillis: Int = 0,
        easing: Easing = FastOutSlowInEasing,
        repeatIterations: Int = 0,
        repeatMode: RepeatMode = RepeatMode.Restart,
        fromX: Float = 0f,
        toX: Float = 1f,
        fromY: Float = 0f,
        toY: Float = 1f,
        transformOrigin: TransformOrigin? = null,
    ) = if (fromX == fromY && toX == toY) {
        scale(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = easing,
            repeatIterations = repeatIterations,
            repeatMode = repeatMode,
            from = fromX,
            to = toX,
            transformOrigin = transformOrigin,
        )
    } else {
        scaleX(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = easing,
            repeatIterations = repeatIterations,
            repeatMode = repeatMode,
            from = fromX,
            to = toX,
            transformOrigin = transformOrigin,
        ) + scaleY(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = easing,
            repeatIterations = repeatIterations,
            repeatMode = repeatMode,
            from = fromY,
            to = toY,
            transformOrigin = transformOrigin,
        )
    }
}