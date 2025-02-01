package com.aghajari.composelayoutanimation

import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.EaseInOutBounce
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing

/**
 * Creates a fall-down animation specification where items fade in while sliding down.
 * Items appear to fall into place from slightly above their final position.
 *
 * The animation combines two parallel transitions:
 * 1. Fade in: From transparent to fully visible
 * 2. Slide down: From a position above (defined by [slideYPercent]) to final position
 *
 * @param initialDelayMillis Delay before the first item starts animating
 * @param durationMillis Duration of both fade and slide animations
 * @param delayMillisBetweenItems Stagger delay between items (defaults to 20% of duration)
 * @param easing Easing curve applied to both fade and slide
 * @param slideYPercent Percentage of item height to start sliding from (negative = above)
 *                     e.g., -0.2f starts 20% above final position
 * @return [LayoutAnimationSpec] configured for fall-down animation
 */
fun fallDownAnimationSpec(
    initialDelayMillis: Int = 0,
    durationMillis: Int = DefaultDurationMillis,
    delayMillisBetweenItems: Int = (durationMillis * 0.2).toInt(),
    easing: Easing = FastOutSlowInEasing,
    slideYPercent: Float = -0.2f,
): LayoutAnimationSpec {
    return LayoutAnimationSpec(
        initialDelayMillis = initialDelayMillis,
        delayMillisBetweenItems = delayMillisBetweenItems,
    ) {
        fade(
            durationMillis = durationMillis,
            easing = easing,
        ) + slideVertically(
            durationMillis = durationMillis,
            easing = easing,
            from = { (it * slideYPercent).toInt() },
        )
    }
}

/**
 * Creates a spiral animation specification where items fade in while scaling up and rotating.
 * Items appear with a spinning entrance effect, starting from invisible and small,
 * then growing to full size while completing a full rotation.
 *
 * The animation combines three parallel transitions:
 * 1. Fade in: From transparent to fully visible
 * 2. Scale: From 0% to 100% size
 * 3. Rotation: A full 360-degree clockwise rotation
 *
 * @param initialDelayMillis Delay before the first item starts animating
 * @param durationMillis Duration for all three animations (fade, scale, and rotation)
 * @param delayMillisBetweenItems Stagger delay between items (defaults to 20% of duration)
 * @param easing Easing curve applied to all transitions
 * @return [LayoutAnimationSpec] configured for spiral animation
 */
fun spiralAnimationSpec(
    initialDelayMillis: Int = 0,
    durationMillis: Int = DefaultDurationMillis,
    delayMillisBetweenItems: Int = (durationMillis * 0.2).toInt(),
    easing: Easing = FastOutSlowInEasing,
): LayoutAnimationSpec {
    return LayoutAnimationSpec(
        initialDelayMillis = initialDelayMillis,
        delayMillisBetweenItems = delayMillisBetweenItems,
    ) {
        fade(
            durationMillis = durationMillis,
            easing = easing,
        ) + scale(
            durationMillis = durationMillis,
            easing = easing,
        ) + rotationZ(
            durationMillis = durationMillis,
            easing = easing,
            from = 360f,
        )
    }
}

/**
 * Creates a bounce animation specification where items fade in while sliding up from bottom.
 * Items appear to bounce into place from below their final position, creating
 * a playful entrance effect.
 *
 * The animation combines two parallel transitions:
 * 1. Fade in: From transparent to fully visible
 * 2. Slide up: From item's full height below (100%) to final position
 *
 * @param initialDelayMillis Delay before the first item starts animating
 * @param durationMillis Duration for both fade and slide animations
 * @param delayMillisBetweenItems Stagger delay between items (defaults to 20% of duration)
 * @param easing Easing curve applied to both transitions, creates bounce effect
 * @return [LayoutAnimationSpec] configured for bounce animation
 */
fun bounceAnimationSpec(
    initialDelayMillis: Int = 0,
    durationMillis: Int = DefaultDurationMillis,
    delayMillisBetweenItems: Int = (durationMillis * 0.2).toInt(),
    easing: Easing = FastOutSlowInEasing,
): LayoutAnimationSpec {
    return LayoutAnimationSpec(
        initialDelayMillis = initialDelayMillis,
        delayMillisBetweenItems = delayMillisBetweenItems,
    ) {
        fade(
            durationMillis = durationMillis,
            easing = easing,
        ) + slideVertically(
            durationMillis = durationMillis,
            easing = easing,
            from = { it },
        )
    }
}

/**
 * Creates a flip animation specification where items fade in while scaling vertically.
 * Items appear to flip open vertically like a card or page, combining a fade effect
 * with vertical scaling for a paper-like entrance.
 *
 * The animation combines two parallel transitions:
 * 1. Fade in: From transparent to fully visible
 * 2. Scale Y: From 0% to 100% height, creating a vertical flip effect
 *
 * @param initialDelayMillis Delay before the first item starts animating
 * @param durationMillis Duration for both fade and scale animations
 * @param delayMillisBetweenItems Stagger delay between items (defaults to 20% of duration)
 * @param easing Easing curve applied to both transitions
 * @return [LayoutAnimationSpec] configured for vertical flip animation
 */
fun flipAnimationSpec(
    initialDelayMillis: Int = 0,
    durationMillis: Int = DefaultDurationMillis,
    delayMillisBetweenItems: Int = (durationMillis * 0.2).toInt(),
    easing: Easing = FastOutSlowInEasing,
): LayoutAnimationSpec {
    return LayoutAnimationSpec(
        initialDelayMillis = initialDelayMillis,
        delayMillisBetweenItems = delayMillisBetweenItems,
    ) {
        fade(
            durationMillis = durationMillis,
            easing = easing,
        ) + scaleY(
            durationMillis = durationMillis,
            easing = easing,
        )
    }
}

/**
 * Creates an origami-like unfolding animation that simulates paper unfolding.
 * Combines X-axis rotation with non-uniform scaling and fade for a 3D paper effect.
 *
 * The animation combines three parallel transitions:
 * 1. Rotation X: From -90° to 0° (60% of duration)
 * 2. Scale: From 70% to 100% width, 0% to 100% height (60% of duration)
 * 3. Fade: From transparent to visible (40% of duration)
 *
 * @param initialDelayMillis Delay before the first item starts animating
 * @param durationMillis Total duration of the animation (default: 1600ms)
 * @param delayMillisBetweenItems Stagger delay between items (default: 10% of duration)
 * @param easing Easing curve applied to rotation and scale transitions
 * @return [LayoutAnimationSpec] configured for origami unfolding animation
 */
fun origamiUnfoldAnimationSpec(
    initialDelayMillis: Int = 0,
    durationMillis: Int = 1600,
    delayMillisBetweenItems: Int = (durationMillis * 0.1).toInt(),
    easing: Easing = FastOutSlowInEasing,
): LayoutAnimationSpec {
    return LayoutAnimationSpec(
        initialDelayMillis = initialDelayMillis,
        delayMillisBetweenItems = delayMillisBetweenItems,
    ) {
        rotationX(
            durationMillis = (durationMillis * 0.6).toInt(),
            easing = easing,
            from = -90f,
            to = 0f,
        ) + scale(
            durationMillis = (durationMillis * 0.6).toInt(),
            easing = easing,
            fromY = 0f,
            toY = 1f,
            fromX = 0.7f,
            toX = 1f,
        ) + fade(
            durationMillis = (durationMillis * 0.4).toInt(),
            easing = LinearEasing,
            from = 0f,
            to = 1f,
        )
    }
}

/**
 * Creates a perspective reveal animation with a 3D rotation effect.
 * Items appear to swing in from the side with perspective depth.
 *
 * The animation combines three parallel transitions:
 * 1. Rotation Y: From 45° to 0° (70% of duration)
 * 2. Translation X: From 300px to 0px (70% of duration)
 * 3. Fade: From transparent to visible (40% of duration)
 *
 * @param initialDelayMillis Delay before the first item starts animating
 * @param durationMillis Total duration of the animation (default: 1500ms)
 * @param delayMillisBetweenItems Stagger delay between items (default: 12% of duration)
 * @param easing Easing curve applied to rotation and translation transitions
 * @return [LayoutAnimationSpec] configured for perspective reveal animation
 */
fun perspectiveRevealAnimationSpec(
    initialDelayMillis: Int = 0,
    durationMillis: Int = 1500,
    delayMillisBetweenItems: Int = (durationMillis * 0.12).toInt(),
    easing: Easing = FastOutSlowInEasing,
): LayoutAnimationSpec {
    return LayoutAnimationSpec(
        initialDelayMillis = initialDelayMillis,
        delayMillisBetweenItems = delayMillisBetweenItems,
    ) {
        rotationY(
            durationMillis = (durationMillis * 0.7).toInt(),
            easing = easing,
            from = 45f,
            to = 0f,
        ) + translationX(
            durationMillis = (durationMillis * 0.7).toInt(),
            easing = easing,
            from = 300f,
            to = 0f,
        ) + fade(
            durationMillis = (durationMillis * 0.4).toInt(),
            easing = LinearEasing,
            from = 0f,
            to = 1f,
        )
    }
}

/**
 * Creates an elastic snapping animation that combines horizontal movement with scaling.
 * Items appear to snap into place with a slight bounce effect.
 *
 * The animation combines three parallel transitions:
 * 1. Translation X: From -200px to 0px (60% of duration)
 * 2. Scale: From 80% to 100% size (60% of duration)
 * 3. Fade: From transparent to visible (30% of duration)
 *
 * @param initialDelayMillis Delay before the first item starts animating
 * @param durationMillis Total duration of the animation (default: 1400ms)
 * @param delayMillisBetweenItems Stagger delay between items (default: 15% of duration)
 * @param easing Easing curve applied to translation and scale transitions
 * @return [LayoutAnimationSpec] configured for elastic snap animation
 */
fun elasticSnapAnimationSpec(
    initialDelayMillis: Int = 0,
    durationMillis: Int = 1400,
    delayMillisBetweenItems: Int = (durationMillis * 0.15).toInt(),
    easing: Easing = FastOutSlowInEasing,
): LayoutAnimationSpec {
    return LayoutAnimationSpec(
        initialDelayMillis = initialDelayMillis,
        delayMillisBetweenItems = delayMillisBetweenItems,
    ) {
        translationX(
            durationMillis = (durationMillis * 0.6).toInt(),
            easing = easing,
            from = -200f,
            to = 0f,
        ) + scale(
            durationMillis = (durationMillis * 0.6).toInt(),
            easing = easing,
            from = 0.8f,
            to = 1f,
        ) + fade(
            durationMillis = (durationMillis * 0.3).toInt(),
            easing = LinearEasing,
            from = 0f,
            to = 1f,
        )
    }
}

/**
 * Creates a cascading drop animation with rotation and vertical movement.
 * Items appear to fall into place with a slight rotational twist.
 *
 * The animation combines three parallel transitions:
 * 1. Translation Y: From -200px to 0px (50% of duration)
 * 2. Rotation Z: From -15° to 0° (50% of duration)
 * 3. Fade: From transparent to visible (30% of duration)
 *
 * @param initialDelayMillis Delay before the first item starts animating
 * @param durationMillis Total duration of the animation (default: 1700ms)
 * @param delayMillisBetweenItems Stagger delay between items (default: 20% of duration)
 * @param easing Easing curve applied to translation and rotation transitions
 * @return [LayoutAnimationSpec] configured for cascade drop animation
 */
fun cascadeDropAnimationSpec(
    initialDelayMillis: Int = 0,
    durationMillis: Int = 1700,
    delayMillisBetweenItems: Int = (durationMillis * 0.2).toInt(),
    easing: Easing = FastOutSlowInEasing,
): LayoutAnimationSpec {
    return LayoutAnimationSpec(
        initialDelayMillis = initialDelayMillis,
        delayMillisBetweenItems = delayMillisBetweenItems,
    ) {
        translationY(
            durationMillis = (durationMillis * 0.5).toInt(),
            easing = easing,
            from = -200f,
            to = 0f,
        ) + rotationZ(
            durationMillis = (durationMillis * 0.5).toInt(),
            easing = easing,
            from = -15f,
            to = 0f,
        ) + fade(
            durationMillis = (durationMillis * 0.3).toInt(),
            easing = LinearEasing,
            from = 0f,
            to = 1f,
        )
    }
}

/**
 * Creates a swivel reveal animation combining 3D rotation with scaling and translation.
 * Items swing in from the side while scaling up, creating a door-like opening effect.
 *
 * The animation combines four parallel transitions:
 * 1. Rotation Y: From -90° to 0° (60% of duration)
 * 2. Scale: From 50% to 100% size (60% of duration)
 * 3. Translation X: From -200px to 0px (60% of duration)
 * 4. Fade: From transparent to visible (40% of duration)
 *
 * @param initialDelayMillis Delay before the first item starts animating
 * @param durationMillis Total duration of the animation (default: 1600ms)
 * @param delayMillisBetweenItems Stagger delay between items (default: 15% of duration)
 * @param easing Easing curve applied to rotation, scale, and translation transitions
 * @return [LayoutAnimationSpec] configured for swivel reveal animation
 */
fun swivelRevealAnimationSpec(
    initialDelayMillis: Int = 0,
    durationMillis: Int = 1600,
    delayMillisBetweenItems: Int = (durationMillis * 0.15).toInt(),
    easing: Easing = FastOutSlowInEasing,
): LayoutAnimationSpec {
    return LayoutAnimationSpec(
        initialDelayMillis = initialDelayMillis,
        delayMillisBetweenItems = delayMillisBetweenItems,
    ) {
        rotationY(
            durationMillis = (durationMillis * 0.6).toInt(),
            easing = easing,
            from = -90f,
            to = 0f,
        ) + scale(
            durationMillis = (durationMillis * 0.6).toInt(),
            easing = easing,
            from = 0.5f,
            to = 1f,
        ) + translationX(
            durationMillis = (durationMillis * 0.6).toInt(),
            easing = easing,
            from = -200f,
            to = 0f,
        ) + fade(
            durationMillis = (durationMillis * 0.4).toInt(),
            easing = LinearEasing,
            from = 0f,
            to = 1f,
        )
    }
}

/**
 * Creates an elegant entrance animation with complex, choreographed transitions.
 * Combines multiple parallel and sequential animations for a sophisticated,
 * smooth entrance effect.
 *
 * The animation consists of three parallel sequences:
 *
 * 1. Main Movement Sequence:
 *    - First: Parallel combination of
 *      * Scale up (30% → 105%)
 *      * Slide up (100px → 0px)
 *      * Rotate Z (-10° → 0°)
 *    - Then: Sequential scaling
 *      * Scale down (105% → 98%)
 *      * Bounce to final size (98% → 100%)
 *
 * 2. 3D Rotation Sequence:
 *    - Y-axis rotation (10° → 0°)
 *    - Followed by X-axis rotation (5° → 0°)
 *
 * 3. Fade Sequence:
 *    - Initial fade in (0% → 70%)
 *    - Followed by subtle opacity adjustments
 *      * Fade up (70% → 100%)
 *      * Slight dim (100% → 95%)
 *      * Final fade (95% → 100%)
 *
 * @param initialDelayMillis Delay before the first item starts animating
 * @param durationMillis Total duration of the animation (default: 1800ms)
 * @param delayMillisBetweenItems Stagger delay between items (default: 8% of duration)
 * @param easing Easing curve applied to transitions
 * @return [LayoutAnimationSpec] configured for elegant entrance animation
 */
fun elegantEntranceAnimationSpec(
    initialDelayMillis: Int = 0,
    durationMillis: Int = 1800,
    delayMillisBetweenItems: Int = (durationMillis * 0.08).toInt(),
    easing: Easing = FastOutSlowInEasing,
): LayoutAnimationSpec {
    return LayoutAnimationSpec(
        initialDelayMillis = initialDelayMillis,
        delayMillisBetweenItems = delayMillisBetweenItems,
    ) {
        together(
            sequence(
                together(
                    scale(
                        durationMillis = (durationMillis * 0.6).toInt(),
                        easing = easing,
                        from = 0.3f,
                        to = 1.05f,
                    ),
                    translationY(
                        durationMillis = (durationMillis * 0.6).toInt(),
                        easing = easing,
                        from = 100f,
                        to = 0f,
                    ),
                    rotationZ(
                        durationMillis = (durationMillis * 0.6).toInt(),
                        easing = easing,
                        from = -10f,
                        to = 0f,
                    )
                ),
                sequence(
                    scale(
                        durationMillis = (durationMillis * 0.2).toInt(),
                        easing = easing,
                        from = 1.05f,
                        to = 0.98f,
                    ),
                    scale(
                        durationMillis = (durationMillis * 0.2).toInt(),
                        easing = EaseInOutBounce,
                        from = 0.98f,
                        to = 1f,
                    )
                )
            ),
            sequence(
                rotationY(
                    durationMillis = (durationMillis * 0.4).toInt(),
                    easing = easing,
                    from = 10f,
                    to = 0f,
                ),
                rotationX(
                    durationMillis = (durationMillis * 0.4).toInt(),
                    easing = easing,
                    from = 5f,
                    to = 0f,
                )
            ),
            sequence(
                fade(
                    durationMillis = (durationMillis * 0.4).toInt(),
                    easing = LinearEasing,
                    from = 0f,
                    to = 0.7f,
                ),
                sequence(
                    fade(
                        durationMillis = (durationMillis * 0.3).toInt(),
                        easing = LinearOutSlowInEasing,
                        from = 0.7f,
                        to = 1f,
                    ),
                    fade(
                        durationMillis = (durationMillis * 0.3).toInt(),
                        easing = easing,
                        from = 1f,
                        to = 0.95f,
                    ),
                    fade(
                        durationMillis = (durationMillis * 0.4).toInt(),
                        easing = easing,
                        from = 0.95f,
                        to = 1f,
                    )
                )
            )
        )
    }
}