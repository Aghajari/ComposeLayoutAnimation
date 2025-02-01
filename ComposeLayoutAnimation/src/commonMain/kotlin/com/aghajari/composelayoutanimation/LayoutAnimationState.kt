package com.aghajari.composelayoutanimation

import androidx.compose.animation.core.Transition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * State holder for layout animations that manages visibility transitions.
 * Controls when animations should start based on visibility changes.
 *
 * The state tracks:
 * - Current visibility
 * - Target visibility
 * - Transition state
 * - Running animations
 */
@Stable
class LayoutAnimationState(
    initialVisibility: Boolean = false,
) {

    /** Internal transition object managing the animation */
    internal var transition: Transition<Boolean>? = null

    /** Mutable state holding the target visibility */
    val visible = mutableStateOf(initialVisibility)

    /**
     * The current visibility state of the animation.
     * Returns the transition's current state if available, otherwise falls back to [visible].
     *
     * This will always be the initialState of the transition until the transition is finished.
     * Once the transition is finished, [isCurrentlyVisible] will be set to [visible].
     */
    val isCurrentlyVisible: Boolean
        get() = transition?.currentState ?: visible.value

    /**
     * The target visibility state of the animation.
     * Returns the transition's target state if available, otherwise falls back to [visible].
     *
     * When starting a fade-in:
     *  - [isGoingToVisible] becomes true immediately
     *  while [isCurrentlyVisible] animates to true.
     *
     * When starting a fade-out:
     *  - [isGoingToVisible] becomes false immediately
     *  while [isCurrentlyVisible] animates to false.
     */
    val isGoingToVisible: Boolean
        get() = transition?.targetState ?: visible.value

    /**
     * List of all running animation states in the current transition.
     * Empty if no transition is active.
     */
    val animations: List<Transition<Boolean>.TransitionAnimationState<*, *>>
        get() = transition?.animations ?: emptyList()

    /**
     * Indicates whether there is any animation running in the transition.
     */
    val isRunning: Boolean
        get() = transition?.isRunning ?: false
}

/**
 * Creates and remembers a [LayoutAnimationState] with initial and target visibility states.
 * Automatically triggers the transition from [fromVisibility] to [toVisibility].
 *
 * Example usage:
 * ```
 * val state = rememberLayoutAnimationState(
 *     fromVisibility = false,  // Start hidden
 *     toVisibility = true,     // Animate to visible
 * )
 * ```
 *
 * @param fromVisibility Initial visibility state
 * @param toVisibility Target visibility state to animate to
 * @return A remembered [LayoutAnimationState] instance
 */
@Composable
fun rememberLayoutAnimationState(
    fromVisibility: Boolean = false,
    toVisibility: Boolean = true,
): LayoutAnimationState {
    val layoutAnimationState = remember {
        LayoutAnimationState(initialVisibility = fromVisibility)
    }
    LaunchedEffect(toVisibility) {
        layoutAnimationState.visible.value = toVisibility
    }
    return layoutAnimationState
}