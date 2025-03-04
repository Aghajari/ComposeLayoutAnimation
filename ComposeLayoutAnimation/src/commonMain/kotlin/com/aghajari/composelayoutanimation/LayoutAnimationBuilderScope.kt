package com.aghajari.composelayoutanimation

import androidx.compose.runtime.Immutable
import com.aghajari.composelayoutanimation.transitions.CustomBuilder
import com.aghajari.composelayoutanimation.transitions.ExpandBuilder
import com.aghajari.composelayoutanimation.transitions.FadeBuilder
import com.aghajari.composelayoutanimation.transitions.RotationBuilder
import com.aghajari.composelayoutanimation.transitions.ScaleBuilder
import com.aghajari.composelayoutanimation.transitions.SlideBuilder
import com.aghajari.composelayoutanimation.transitions.TranslationBuilder

/**
 * A scope class for building layout animations in a structured way.
 * Combines multiple transition builders and provides methods for composing animations.
 *
 * Transitions can be combined in two ways:
 * 1. Parallel (Together) - Multiple transitions running simultaneously:
 * ```
 * // All these expressions are equivalent:
 * fade() + expand()          // Using + operator
 * fade() with expand()       // Using 'with' infix function
 * together(fade(), expand()) // Using together() function
 * ```
 *
 * 2. Sequential - Transitions running one after another:
 * ```
 * // Both expressions are equivalent:
 * fade() then expand()       // Using 'then' infix function
 * sequence(fade(), expand()) // Using sequence() function
 * ```
 *
 * This scope implements various transition builders:
 * - [FadeBuilder]: Alpha transitions
 * - [SlideBuilder]: Slide animations
 * - [ScaleBuilder]: Scale transformations
 * - [RotationBuilder]: Rotation animations
 * - [TranslationBuilder]: Translation movements
 * - [ExpandBuilder]: Size expansion/collapse
 * - [CustomBuilder]: Custom transitions
 */
@Immutable
sealed class LayoutAnimationBuilderScope :
    FadeBuilder,
    SlideBuilder,
    ScaleBuilder,
    RotationBuilder,
    TranslationBuilder,
    ExpandBuilder,
    CustomBuilder {

    /**
     * Combines multiple animations to run simultaneously.
     * All animations in the group will start at the same time.
     *
     * Example:
     * ```
     * together(
     *     fade(from = 0f, to = 1f),
     *     scale(from = 0.8f, to = 1f),
     * )
     * ```
     *
     * @param first The first animation in the group
     * @param others Additional animations to run simultaneously
     * @return A combined [LayoutTransition] where all transitions run in parallel
     */
    fun together(
        first: LayoutTransition,
        vararg others: LayoutTransition,
    ): LayoutTransition {
        var out = first
        others.forEach { out += it }
        return out
    }

    /**
     * Creates a sequence of animations where each animation
     * starts after the previous one completes.
     *
     * Example:
     * ```
     * sequence(
     *     fade(durationMillis = 300) + expand(),    // These run together
     *     translate(),  // This starts after the previous group completes
     * )
     * ```
     *
     * @param first The first animation in the sequence
     * @param others Subsequent animations to run in sequence
     * @return A combined [LayoutTransition] where transitions run in sequence
     */
    fun sequence(
        first: LayoutTransition,
        vararg others: LayoutTransition
    ): LayoutTransition {
        var out = first
        others.forEach { other ->
            val duration = out.getDuration()
            other.transitions.forEach {
                it.sequenceDelay(duration)
            }
            out += other
        }
        return out
    }
}