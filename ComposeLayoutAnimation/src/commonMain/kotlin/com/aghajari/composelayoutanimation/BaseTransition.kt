package com.aghajari.composelayoutanimation

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlin.math.max

/**
 * Base class for layout animation transitions.
 *
 * @param T The type of value being animated
 */

@Immutable
internal abstract class BaseTransition<T> {

    /**
     * The type identifier of the transition, derived from the implementing class name.
     * This property is used to determine when a transition should be applied:
     *
     * 1. First transition of its type: Always applies immediately, regardless of delay
     *    - Initial value is applied even during the delay period
     *    - Acts as the base state for this type of transition
     *
     * 2. Subsequent transitions of the same type: Only apply when their animation has started
     *    - Will override the previous transition of the same type
     *    - Respect their delay before starting
     *    - Won't apply any changes until their animation actually begins
     *
     * Example:
     * ```
     * fade(
     *   durationMillis = 300,
     *   delayMillis = 25,
     *   from = 0f,
     *   to = 1f,
     * ) + fade(
     *   durationMillis = 300,
     *   delayMillis = 300,
     *   from = 1f,
     *   to = 0f,
     * )
     * ```
     * In this example:
     * - First fade: Immediately sets alpha to 0f, then animates to 1f after 25ms delay
     * - Second fade: Waits 300ms, then starts animating from 1f to 0f, overriding the first fade
     */
    val type: String = this::class.simpleName ?: "Unknown"

    /**
     * Configuration for building the animation specification
     */
    abstract val finiteAnimationSpecBuilder: FiniteAnimationSpecBuilder

    /**
     * Evaluator for interpolating animation values of type T
     */
    abstract val evaluator: Evaluator<T>

    /**
     * Determines whether this transition modifies GraphicsLayer properties or layout properties.
     *
     * This flag establishes a mutual exclusion between two types of animations:
     * 1. GraphicsLayer modifications (when true):
     *   Only [applyGraphicsLayer] will be called
     *    - Used for visual transformations like alpha, scale, rotation
     *    - Doesn't affect actual layout measurements
     *
     * 2. Layout modifications (when false):
     *   Only [applySize] and [applyOffset] will be called
     *    - Used for actual layout changes like size and position
     *    - Affects layout measurements
     */
    open val isGraphicsLayer: Boolean = false

    /**
     * Delay in milliseconds to be applied when this transition is part of a sequence.
     * Used to ensure sequential execution when multiple transitions are chained.
     */
    internal var sequenceDelay = 0
        private set

    /**
     * Updates the target value for the animation.
     *
     * @param animation The animation runner instance
     * @return State object containing the animated value,
     *  or null if not applicable
     */
    open fun updateTarget(animation: LayoutAnimationRunner<T>): State<T?>? = null

    /**
     * Applies graphics layer transformations for the animation.
     * Only called when [isGraphicsLayer] is true.
     *
     * @param animation The animation runner instance
     * @return Lambda function containing graphics layer modifications,
     *  or null if not applicable
     */
    open fun applyGraphicsLayer(
        animation: LayoutAnimationRunner<T>,
    ): (() -> (GraphicsLayerScope.() -> Unit))? = null

    /**
     * Applies size modifications for the animation.
     * Only called when [isGraphicsLayer] is false.
     *
     * @param animation The animation runner instance
     * @return Lambda function containing size modifications and alignment,
     *  or null if not applicable
     */
    open fun applySize(
        animation: LayoutAnimationRunner<T>,
    ): ((IntSize) -> Pair<State<IntSize?>, Alignment?>)? = null

    /**
     * Applies offset modifications for the animation.
     * Only called when [isGraphicsLayer] is false.
     *
     * @param animation The animation runner instance
     * @return Lambda function containing offset modifications, or null if not applicable
     */
    open fun applyOffset(
        animation: LayoutAnimationRunner<T>,
    ): ((IntSize) -> State<IntOffset?>)? = null

    /**
     * Gets the total duration of the animation including both
     * the animation specification duration and any sequence delay.
     *
     * @return Total duration in milliseconds
     */
    fun getDuration() = finiteAnimationSpecBuilder.getDuration() + sequenceDelay

    /**
     * Sets a delay for this transition when it's part of a sequence of transitions.
     * This delay ensures that the transition starts after the previous transition(s)
     * in the sequence have completed their animations.
     *
     * For example, if Transition A takes 300ms and Transition B should start after A:
     * - Transition A: no sequence delay
     * - Transition B: sequenceDelay(300) to start after A completes
     *
     * @param delayInMillis The delay in milliseconds to be added to
     *  the initial start of this transition
     */
    fun sequenceDelay(delayInMillis: Int) {
        sequenceDelay += delayInMillis
    }
}

/**
 * Base class for graphics layer-specific transitions.
 * Handles animations that modify GraphicsLayer properties.
 */
@Immutable
internal abstract class BaseGraphicsLayerTransition : BaseTransition<Float>() {

    abstract val from: Float
    abstract val to: Float
    override val evaluator = FloatEvaluator
    override val isGraphicsLayer = true

    /**
     * Applies the specified graphics layer transformation.
     *
     * @param value The current animation value
     */
    abstract fun GraphicsLayerScope.applyGraphicsLayer(value: Float)

    override fun updateTarget(
        animation: LayoutAnimationRunner<Float>
    ): State<Float?> {
        return animation.animate(
            initialValue = from,
            targetValue = to,
        )
    }

    override fun applyGraphicsLayer(
        animation: LayoutAnimationRunner<Float>,
    ): () -> GraphicsLayerScope.() -> Unit = {
        val state = updateTarget(animation)
        val block: GraphicsLayerScope.() -> Unit = {
            applyGraphicsLayer(state.value ?: from)
        }
        block
    }
}

/**
 * Configuration class for finite animation specifications.
 *
 * @property durationMillis Duration of the animation in milliseconds
 * @property delayMillis Initial delay before animation starts in milliseconds
 * @property easing The easing curve to be applied to the animation
 * @property repeatIterations Number of times the animation should repeat
 * @property repeatMode The repeat behavior (REVERSE or RESTART)
 */
@Immutable
internal data class FiniteAnimationSpecBuilder(
    val durationMillis: Int,
    val delayMillis: Int,
    val easing: Easing,
    val repeatIterations: Int,
    val repeatMode: RepeatMode,
) {

    /**
     * Builds and returns an animation specification based on the configuration.
     *
     * The method creates one of two types of animations:
     * 1. A simple [tween] animation when [repeatIterations] is 1 or less
     * 2. A [repeatable] animation when [repeatIterations] is greater than 1
     *
     * For non-repeating animations (tween):
     * - The [offsetDelayMillis] is added to the initial [delayMillis]
     * - Total delay will be (delayMillis + offsetDelayMillis)
     *
     * For repeating animations:
     * - The [offsetDelayMillis] is applied only to the initial start of the animation sequence
     * - Subsequent repetitions will only use the base [delayMillis]
     * - The animation repeats according to the specified [repeatMode]
     *
     * @param offsetDelayMillis Additional delay to be added before the animation starts.
     *                         For repeatable animations, this only affects the initial start.
     * @return FiniteAnimationSpec<Float> configured as either a tween or repeatable animation
     */
    fun build(offsetDelayMillis: Int): FiniteAnimationSpec<Float> {
        return if (repeatIterations <= 1) {
            tween(
                durationMillis = durationMillis,
                delayMillis = delayMillis + offsetDelayMillis,
                easing = easing,
            )
        } else {
            repeatable(
                iterations = repeatIterations,
                repeatMode = repeatMode,
                initialStartOffset = StartOffset(
                    offsetMillis = offsetDelayMillis,
                    offsetType = StartOffsetType.Delay,
                ),
                animation = tween(
                    durationMillis = durationMillis,
                    delayMillis = delayMillis,
                    easing = easing,
                ),
            )
        }
    }

    fun getDuration(): Int {
        return (durationMillis + delayMillis) * max(1, repeatIterations)
    }
}

/**
 * Interface defining the contract for running layout animations.
 *
 * @param T The type of value being animated
 */
@Immutable
internal interface LayoutAnimationRunner<T> {
    /**
     * Indicates whether the animation has completed.
     */
    val hasFinished: Boolean

    /**
     * Indicates whether the animation has begun.
     */
    val hasStarted: Boolean

    /**
     * Initiates an animation between two values.
     *
     * @param initialValue The starting value of the animation
     * @param targetValue The ending value of the animation
     * @return State object containing the current animated value
     */
    fun animate(
        initialValue: T,
        targetValue: T,
    ): State<T?>
}