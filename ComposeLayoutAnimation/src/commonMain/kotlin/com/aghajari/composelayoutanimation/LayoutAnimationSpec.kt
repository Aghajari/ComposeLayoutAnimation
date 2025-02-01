package com.aghajari.composelayoutanimation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

/**
 * Specification for layout animations that can be applied to multiple items in a layout.
 * Controls the timing and sequencing of animations across multiple items.
 *
 * @property initialDelayMillis Delay before the first item starts animating
 * @property delayMillisBetweenItems Stagger delay between subsequent items' animations
 * @property animation The animation built using [LayoutAnimationBuilderScope]
 * @see LayoutAnimation Entry point
 * @see LayoutAnimationBuilderScope Animation builder scope
 */
@Immutable
class LayoutAnimationSpec(
    val initialDelayMillis: Int = 0,
    val delayMillisBetweenItems: Int = 50,
    animationBuilder: LayoutAnimationBuilderScope.() -> LayoutTransition,
) {
    internal val animation = LayoutAnimationBuilderScopeImpl.animationBuilder()

    /**
     * Calculates the total duration including initial delay and animation duration.
     * @return Total duration in milliseconds
     */
    fun getDuration(): Int = initialDelayMillis + animation.getDuration()
}

/**
 * Base class for layout animations that can be combined and sequenced.
 */
@Immutable
sealed class LayoutTransition {
    internal abstract val current: BaseTransition<*>
    internal abstract val parentTransitions: List<BaseTransition<*>>
    internal abstract val clip: Boolean

    /**
     * All transitions in this animation,
     * including parent transitions and current transition
     */
    internal val transitions: List<BaseTransition<*>>
        get() = parentTransitions + listOf(current)

    /**
     * Combines two animations to run simultaneously.
     *
     * Example:
     * ```
     * fade() + scale()  // Fade and scale happen together
     * ```
     *
     * @param other Animation to combine with this one
     * @return Combined animation where both run simultaneously
     */
    @Stable
    operator fun plus(other: LayoutTransition): LayoutTransition {
        return LayoutTransitionImpl(
            parentTransitions = transitions + other.parentTransitions,
            current = other.current,
            clip = clip || other.clip,
        )
    }

    /**
     * Gets the duration of the longest transition in this animation.
     * @return Duration in milliseconds
     */
    fun getDuration(): Int = transitions.maxOf {
        it.getDuration()
    }
}

/**
 * Chains two animations to run sequentially.
 * The second animation starts after the first one completes.
 * Alternative syntax for [LayoutAnimationBuilderScope.sequence] function.
 *
 * Example:
 * ```
 * fade() then scale()  // Scale starts after fade completes
 * ```
 *
 * @param that The animation to run after this one
 * @return Combined sequential animation
 */
infix fun LayoutTransition.then(that: LayoutTransition): LayoutTransition {
    return LayoutAnimationBuilderScopeImpl.run {
        sequence(this@then, that)
    }
}

/**
 * Combines two animations to run simultaneously.
 * Alternative syntax for the [LayoutTransition.plus] operator and
 * [LayoutAnimationBuilderScope.together] function.
 *
 * Example:
 * ```
 * fade() with scale()  // Equivalent to fade() + scale()
 * ```
 *
 * @param that The animation to run together with this one
 * @return Combined parallel animation
 */
infix fun LayoutTransition.with(that: LayoutTransition): LayoutTransition {
    return LayoutAnimationBuilderScopeImpl.run {
        this@with + that
    }
}

@Immutable
internal class LayoutTransitionImpl(
    override val current: BaseTransition<*>,
    override val parentTransitions: List<BaseTransition<*>> = listOf(),
    override val clip: Boolean = false,
) : LayoutTransition()

@Immutable
private data object LayoutAnimationBuilderScopeImpl :
    LayoutAnimationBuilderScope()