package com.aghajari.composelayoutanimation

import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

/**
 * A layout animation system for Jetpack Compose that provides an equivalent to Android's ViewGroup
 * layoutAnimation, allowing child items to animate when they enter the composition. The library
 * offers a simple yet powerful API for creating staggered entrance animations for layout items.
 *
 * Example usage:
 * ```
 * Column {
 *     LayoutAnimation(
 *         animationSpec = LayoutAnimationSpec(
 *             initialDelayMillis = 100,
 *             delayMillisBetweenItems = 50,
 *         ) {
 *             fade(from = 0f, to = 1f) with
 *                  translationY(from = 0f, to = 100f) then
 *                  translationY(from = 100f, to = 0f)
 *         },
 *     ) {
 *         repeat(5) {
 *             Item(Modifier.animateLayoutItem())
 *         }
 *     }
 * }
 * ```
 *
 * @param animationSpec Specification defining the animations and timing
 * @param state State controlling the visibility and animation transitions
 * @param label Debug label for the animation transition
 * @param content Composable content with access to animation scope
 * @see LayoutAnimationSpec Animation configuration
 * @see LayoutAnimationBuilderScope Animation builder scope
 * @see LayoutAnimationState Animation state controller
 * @author Aghajari
 */
@Composable
fun LayoutAnimation(
    animationSpec: LayoutAnimationSpec,
    state: LayoutAnimationState = rememberLayoutAnimationState(),
    label: String = "LayoutAnimation",
    content: @Composable LayoutAnimationScope.() -> Unit,
) {
    val transition = updateTransition(state.visible.value, label).also {
        state.transition = it
    }
    val scope = remember(animationSpec) {
        LayoutAnimationScopeImpl(animationSpec, transition)
    }
    scope.content()
}

/**
 * Scope interface for layout animations providing modifier for [animateLayoutItem].
 */
@LayoutScopeMarker
@Immutable
interface LayoutAnimationScope {

    /**
     * Creates a modifier that applies the animation to a layout item.
     * Each item with this modifier will be animated according to the [LayoutAnimationSpec].
     *
     * Items are animated in sequence with the delay
     * specified in [LayoutAnimationSpec.delayMillisBetweenItems].
     *
     * Example:
     * ```
     * LayoutAnimation(animationSpec) {
     *     // First item animates immediately
     *     Item(Modifier.animateLayoutItem())
     *
     *     // Second item animates after delay
     *     Item(Modifier.animateLayoutItem())
     *
     *     // Third item animates after another delay
     *     Item(Modifier.animateLayoutItem())
     * }
     * ```
     *
     * @param label Optional debug label for the item's animation
     * @return Modified [Modifier] with animation capabilities
     * @see LayoutAnimation
     */
    fun Modifier.animateLayoutItem(
        label: String? = null,
    ): Modifier
}