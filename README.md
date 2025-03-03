# ComposeLayoutAnimation
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.aghajari/ComposeLayoutAnimation.svg?label=Maven%20Central)](https://search.maven.org/artifact/io.github.aghajari/ComposeLayoutAnimation/1.0.0/aar)
[![Join the chat at https://gitter.im/Aghajari/community](https://badges.gitter.im/Aghajari/community.svg)](https://gitter.im/Aghajari/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

A Jetpack Compose library equivalent to ViewGroup's layoutAnimation, providing staggered entrance animations for layout children with support for both pre-built and custom animations.

**ComposeLayoutAnimation** simplifies the process of adding entrance animations to layout children in Jetpack Compose. Just like ViewGroup's layoutAnimation in traditional Android development, this library allows you to define an animation that automatically applies to all children of a layout with configurable delay between items.

The library provides two types of animations:
- **GraphicsLayer Modifications**: Visual-only animations like fade, scale, and rotation that don't affect layout measurements
- **Layout Modifications**: Animations that affect actual layout like slide and expand

<img src="./images/pre_built.gif" width="400"/>

## Features
- Staggered animations for layout children
- Pre-built animation templates
- Custom animation support
- Simple API for combining animations
- Layout and GraphicsLayer animations
- Supports all Compose Layouts including `Column`, `Row`, `LazyColumn`, `LazyRow`, and `LazyVerticalGrid`

## Installation

**ComposeLayoutAnimation** is available on `mavenCentral()`, Add the dependency to your app's `build.gradle`:

```groovy
dependencies {
    implementation 'io.github.aghajari:ComposeLayoutAnimation:1.0.1'
}
```

## Usage
Basic usage with a simple animation:

```kotlin
Column {
    LayoutAnimation(
        animationSpec = LayoutAnimationSpec(
            delayMillisBetweenItems = 50,
        ) {
            fade(from = 0f, to = 1f) with
                    translationY(from = 0f, to = 100f) then
                    translationY(from = 100f, to = 0f)
        },
    ) {
        repeat(5) { index ->
            CardItem(
                text = "Item $index",
                modifier = Modifier.animateLayoutItem(),
            )
        }
    }
}
```

<b>Note:</b> Use `Modifier.animateLazyLayoutItem()` for Lazy layouts

Output:

<img src="./images/custom.gif" width="300"/>

Basic usage with a pre-built animation:
```kotlin
LayoutAnimation(animationSpec = fallDownAnimationSpec()) {
  ...
}
```

## Pre-built Animations
Ready-to-use animations for common scenarios:

- `fallDownAnimationSpec()`: Items fade in while falling from above
- `bounceAnimationSpec()`: Items bounce up from bottom
- `spiralAnimationSpec()`: Items spiral in with rotation
- `flipAnimationSpec()`: Items flip in vertically
- `origamiUnfoldAnimationSpec()`: 3D paper unfolding effect
- `perspectiveRevealAnimationSpec()`: 3D perspective swing effect
- `elasticSnapAnimationSpec()`: Elastic snapping from side
- `cascadeDropAnimationSpec()`: Cascading drop with rotation
- `swivelRevealAnimationSpec()`: 3D door-like opening effect
- `elegantEntranceAnimationSpec()`: Sophisticated entrance with combined effects

## Animation Composition
Combine animations using various operators:

```kotlin
// Parallel animations
fade() + scale()           // Using +
fade() with scale()        // Using 'with' infix function
together(fade(), scale())  // Using together()

// Sequential animations
fade() then scale()        // Using 'then' infix function
sequence(fade(), scale())  // Using sequence()
```

## Animation Configuration
Every animation, whether pre-built or custom, can be configured with:
- `durationMillis`: Duration of the animation
- `delayMillis`: Delay before the animation starts
- `delayMillisBetweenItems`: Stagger delay between items
- `easing`: Easing curve for the animation
- `repeatMode`: How the animation should repeat (Restart or Reverse)
- `repeatIterations`: Number of times to repeat

## Create Custom Animations
Share your animations with us :))

``` kotlin
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
```

<img src="./images/cascade.gif" width="400"/>

License
=======

    Copyright 2025 Amir Hossein Aghajari
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

<br>
<div align="center">
  <img width="64" alt="LCoders | AmirHosseinAghajari" src="https://user-images.githubusercontent.com/30867537/90538314-a0a79200-e193-11ea-8d90-0a3576e28a18.png">
  <br><a>Amir Hossein Aghajari</a> • <a href="mailto:amirhossein.aghajari.82@gmail.com">Email</a> • <a href="https://github.com/Aghajari">GitHub</a>
</div>
