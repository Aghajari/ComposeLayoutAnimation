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
private data class RotationZ(
    override val from: Float,
    override val to: Float,
    val transformOrigin: TransformOrigin?,
    override val finiteAnimationSpecBuilder: FiniteAnimationSpecBuilder,
) : BaseGraphicsLayerTransition() {

    override fun GraphicsLayerScope.applyGraphicsLayer(value: Float) {
        this.rotationZ = value
        this@RotationZ.transformOrigin?.let {
            this.transformOrigin = it
        }
    }
}

@Immutable
private data class RotationX(
    override val from: Float,
    override val to: Float,
    val transformOrigin: TransformOrigin?,
    override val finiteAnimationSpecBuilder: FiniteAnimationSpecBuilder,
) : BaseGraphicsLayerTransition() {

    override fun GraphicsLayerScope.applyGraphicsLayer(value: Float) {
        this.rotationX = value
        this@RotationX.transformOrigin?.let {
            this.transformOrigin = it
        }
    }
}

@Immutable
private data class RotationY(
    override val from: Float,
    override val to: Float,
    val transformOrigin: TransformOrigin?,
    override val finiteAnimationSpecBuilder: FiniteAnimationSpecBuilder,
) : BaseGraphicsLayerTransition() {

    override fun GraphicsLayerScope.applyGraphicsLayer(value: Float) {
        this.rotationY = value
        this@RotationY.transformOrigin?.let {
            this.transformOrigin = it
        }
    }
}

internal interface RotationBuilder {

    /**
     * Creates a 2D rotation animation around the Z-axis.
     * This is the traditional 2D rotation in the plane of the screen.
     *
     * Example:
     * ```
     * // Full clockwise rotation
     * rotationZ(from = 0f, to = 360f)
     *
     * // Rotate around top-left corner
     * rotationZ(
     *     from = 0f,
     *     to = 90f,
     *     transformOrigin = TransformOrigin(0f, 0f),
     * )
     * ```
     *
     * @param durationMillis Animation duration
     * @param delayMillis Initial delay before animation starts
     * @param easing Easing curve for the animation
     * @param repeatIterations Number of repetitions (0 = no repeat)
     * @param repeatMode How the animation should repeat
     * @param from Starting angle in degrees
     * @param to Target angle in degrees
     * @param transformOrigin Point around which rotation occurs (null = center)
     * @return A [LayoutTransition] configured for the rotation transition
     */
    fun rotationZ(
        durationMillis: Int = DefaultDurationMillis,
        delayMillis: Int = 0,
        easing: Easing = FastOutSlowInEasing,
        repeatIterations: Int = 0,
        repeatMode: RepeatMode = RepeatMode.Restart,
        from: Float = 360f,
        to: Float = 0f,
        transformOrigin: TransformOrigin? = null,
    ): LayoutTransition = LayoutTransitionImpl(
        RotationZ(
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
     * Creates a 3D rotation animation around the X-axis.
     * Produces a flipping effect around the horizontal axis.
     *
     * Example:
     * ```
     * // Flip forward
     * rotationX(from = 0f, to = 180f)
     * ```
     *
     * @param durationMillis Animation duration
     * @param delayMillis Initial delay before animation starts
     * @param easing Easing curve for the animation
     * @param repeatIterations Number of repetitions (0 = no repeat)
     * @param repeatMode How the animation should repeat
     * @param from Starting angle in degrees
     * @param to Target angle in degrees
     * @param transformOrigin Point around which rotation occurs (null = center)
     * @return A [LayoutTransition] configured for the rotation transition
     */
    fun rotationX(
        durationMillis: Int = DefaultDurationMillis,
        delayMillis: Int = 0,
        easing: Easing = FastOutSlowInEasing,
        repeatIterations: Int = 0,
        repeatMode: RepeatMode = RepeatMode.Restart,
        from: Float = 360f,
        to: Float = 0f,
        transformOrigin: TransformOrigin? = null,
    ): LayoutTransition = LayoutTransitionImpl(
        RotationX(
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
     * Creates a 3D rotation animation around the Y-axis.
     * Produces a flipping effect around the vertical axis.
     *
     * Example:
     * ```
     * // Flip sideways
     * rotationY(from = 0f, to = 180f)
     *
     * // Partial rotation from edge
     * rotationY(
     *     from = 0f,
     *     to = 45f,
     *     transformOrigin = TransformOrigin(1f, 0.5f),
     * )
     * ```
     *
     * @param durationMillis Animation duration
     * @param delayMillis Initial delay before animation starts
     * @param easing Easing curve for the animation
     * @param repeatIterations Number of repetitions (0 = no repeat)
     * @param repeatMode How the animation should repeat
     * @param from Starting angle in degrees
     * @param to Target angle in degrees
     * @param transformOrigin Point around which rotation occurs (null = center)
     * @return A [LayoutTransition] configured for the rotation transition
     */
    fun rotationY(
        durationMillis: Int = DefaultDurationMillis,
        delayMillis: Int = 0,
        easing: Easing = FastOutSlowInEasing,
        repeatIterations: Int = 0,
        repeatMode: RepeatMode = RepeatMode.Restart,
        from: Float = 360f,
        to: Float = 0f,
        transformOrigin: TransformOrigin? = null,
    ): LayoutTransition = LayoutTransitionImpl(
        RotationY(
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
}