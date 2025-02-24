package com.aghajari.composelayoutanimation

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.InternalAnimationApi
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.constrain

internal class LayoutAnimationScopeImpl(
    private val animationSpec: LayoutAnimationSpec,
    private val transition: Transition<Boolean>,
) : LayoutAnimationScope {

    private var startTime: Long? = null
    private var duration: Int = animationSpec.getDuration()

    private var childIndex = 0
    private fun nextChild(): Int {
        return childIndex++
    }

    override fun Modifier.animateLazyLayoutItem(label: String?): Modifier {
        return if (transition.currentState.not()) {
            animateLayoutItem(label = label)
        } else this
    }

    override fun Modifier.animateLayoutItem(
        label: String?,
    ) = this.composed {
        val index = remember(currentCompositeKeyHash) { nextChild() }
        if (isRunningAnimation(index, transition)) {
            val transitions = animationSpec.animation.transitions
            val childLabel = label ?: "Child-$index"
            val itemDelay = with(animationSpec) {
                initialDelayMillis + delayMillisBetweenItems * index
            }

            Modifier
                .graphicsLayer(clip = animationSpec.animation.clip)
                .then(
                    createAnimatedLayout(
                        transitions = transitions,
                        itemDelay = itemDelay,
                        transition = transition,
                        label = childLabel,
                        onStartAnimation = ::onStartAnimation,
                    ).invoke(),
                )
        } else {
            Modifier
        }
    }

    private fun onStartAnimation() {
        if (startTime == null) {
            startTime = System.currentTimeMillis()
        }
    }

    @OptIn(InternalAnimationApi::class)
    private fun isRunningAnimation(
        index: Int,
        transition: Transition<Boolean>,
    ): Boolean {
        return if (transition.isSeeking || startTime == null) {
            true
        } else {
            val diff = System.currentTimeMillis() - requireNotNull(startTime)
            val itemDelay = animationSpec.delayMillisBetweenItems * index
            diff <= (duration + itemDelay)
        }
    }
}

private class DeferredFractionBasedAnimation<T>(
    private val evaluator: Evaluator<T>,
) : LayoutAnimationRunner<T> {

    private var initialValue: T? = null
    private var targetValue: T? = null

    val hasInitialized = mutableStateOf(false)
    private val currentValue = mutableStateOf<T?>(null)

    override val hasFinished: Boolean
        get() = currentValue.value == targetValue

    override val hasStarted: Boolean
        get() = currentValue.value != null &&
                currentValue.value != initialValue

    override fun animate(
        initialValue: T,
        targetValue: T,
    ): State<T?> {
        this.initialValue = initialValue
        this.targetValue = targetValue
        hasInitialized.value = true
        return currentValue
    }

    fun updateValue(fraction: Float) {
        currentValue.value = evaluator.evaluate(
            start = requireNotNull(initialValue),
            stop = requireNotNull(targetValue),
            fraction = fraction,
        )
    }
}

private data class LayoutAnimationApplier<T>(
    val type: String,
    val animation: LayoutAnimationRunner<T>,
    val updateTargetInvoker: () -> State<T?>?,
    val graphicsLayerInvoker: () -> (() -> GraphicsLayerScope.() -> Unit)?,
    val sizeInvoker: () -> ((IntSize) -> Pair<State<IntSize?>, Alignment?>)?,
    val offsetInvoker: () -> ((IntSize) -> State<IntOffset?>)?,
)

@Composable
private fun <T> BaseTransition<T>.createApplier(
    transition: Transition<Boolean>,
    itemDelay: Int,
    label: String,
    onStart: () -> Unit,
): LayoutAnimationApplier<T> {
    val animation = transition.createAnimation(
        evaluator = evaluator,
        label = label + " " + this.type,
        onStart = onStart,
    ) { finiteAnimationSpecBuilder.build(itemDelay + sequenceDelay) }

    return LayoutAnimationApplier(
        type = if (sequenceDelay > 0) SequenceType else this.type,
        animation = animation,
        updateTargetInvoker = { updateTarget(animation) },
        graphicsLayerInvoker = { applyGraphicsLayer(animation) },
        sizeInvoker = { applySize(animation) },
        offsetInvoker = { applyOffset(animation) },
    )
}

@Composable
private fun <T> Transition<Boolean>.createAnimation(
    evaluator: Evaluator<T>,
    label: String,
    onStart: () -> Unit,
    transitionSpec: () -> FiniteAnimationSpec<Float>,
): DeferredFractionBasedAnimation<T> {
    val animInfo = remember {
        DeferredFractionBasedAnimation(evaluator = evaluator)
    }
    val fraction = animateFloat(
        label = label,
        transitionSpec = { transitionSpec() },
    ) { visible ->
        if (visible && animInfo.hasInitialized.value) 1f else 0f
    }

    if (animInfo.hasInitialized.value) {
        animInfo.updateValue(fraction.value)
        LaunchedEffect(Unit) {
            onStart.invoke()
        }
    }
    return animInfo
}

@Composable
private fun createAnimatedLayout(
    transitions: List<BaseTransition<*>>,
    itemDelay: Int,
    transition: Transition<Boolean>,
    label: String,
    onStartAnimation: () -> Unit,
): () -> LayoutAnimationElement {
    val layoutAnimations = mutableListOf<LayoutAnimationApplier<*>>()
    val graphicsLayerBlocks = mutableListOf<LayoutAnimationApplier<*>>()

    transitions.forEach {
        val applier = it.createApplier(
            transition = transition,
            itemDelay = itemDelay,
            label = label,
            onStart = onStartAnimation,
        )
        if (it.isGraphicsLayer) {
            graphicsLayerBlocks.add(applier)
        } else {
            layoutAnimations.add(applier)
        }
    }

    val graphicsLayerBlock = createGraphicsLayerBlock(blocks = graphicsLayerBlocks)
    return {
        LayoutAnimationElement(
            layoutAnimations = layoutAnimations,
            graphicsLayerBlock = graphicsLayerBlock,
        )
    }
}

@Composable
private fun createGraphicsLayerBlock(
    blocks: List<LayoutAnimationApplier<*>>,
): () -> (GraphicsLayerScope.() -> Unit) = {
    val scopes = buildList {
        blocks.forEach { applier ->
            applier.graphicsLayerInvoker()?.let { scope ->
                add(applier to scope.invoke())
            }
        }
    }

    val block: GraphicsLayerScope.() -> Unit = {
        val usedTypes = mutableSetOf(SequenceType)

        scopes.forEach { (applier, scope) ->
            if (usedTypes.add(applier.type) ||
                applier.animation.hasStarted
            ) {
                this.apply(scope)
            } else {
                applier.updateTargetInvoker()
            }
        }
    }
    block
}

private class LayoutAnimationModifierNode(
    var layoutAnimations: List<LayoutAnimationApplier<*>>,
    var graphicsLayerBlock: () -> (GraphicsLayerScope.() -> Unit),
) : LayoutModifierNode, Modifier.Node() {

    private var lookaheadConstraintsAvailable = false
    private var lookaheadSize: IntSize? = null
    private var lookaheadConstraints: Constraints = Constraints()
        set(value) {
            lookaheadConstraintsAvailable = true
            field = value
        }

    override fun onAttach() {
        super.onAttach()
        lookaheadConstraintsAvailable = false
        lookaheadSize = null
    }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        if (isLookingAhead) {
            val placeable = measurable.measure(constraints)
            val measuredSize = IntSize(placeable.width, placeable.height)
            lookaheadSize = measuredSize
            lookaheadConstraints = constraints
            return layout(measuredSize.width, measuredSize.height) {
                placeable.place(0, 0)
            }
        } else {
            val layerBlock = graphicsLayerBlock.invoke()
            val placeable = measurable.measure(constraints)
            val measuredSize = IntSize(placeable.width, placeable.height)
            val target = lookaheadSize ?: measuredSize

            var alignment: Alignment? = null
            var animSize: IntSize? = null
            var slideOffset: IntOffset? = null

            val usedTypes = mutableSetOf(SequenceType)
            layoutAnimations.forEach { applier ->
                val shouldApply = usedTypes.add(applier.type) ||
                        applier.animation.hasStarted

                applier.sizeInvoker()?.let {
                    val pair = it.invoke(target)
                    val size = pair.first.value
                    if (shouldApply) {
                        animSize = size ?: animSize
                        if (applier.animation.hasFinished.not()) {
                            alignment = pair.second
                        }
                    }
                }
                applier.offsetInvoker()?.let {
                    val offset = it.invoke(target).value
                    if (shouldApply) {
                        slideOffset = offset ?: slideOffset
                    }
                }
            }

            val currentSize = constraints.constrain(animSize ?: measuredSize)
            val offset = (alignment?.align(target, currentSize, LayoutDirection.Ltr)
                ?: IntOffset.Zero) + (slideOffset ?: IntOffset.Zero)
            return layout(currentSize.width, currentSize.height) {
                placeable.placeWithLayer(offset.x, offset.y, 0f, layerBlock)
            }
        }
    }
}

private data class LayoutAnimationElement(
    var layoutAnimations: List<LayoutAnimationApplier<*>>,
    var graphicsLayerBlock: () -> (GraphicsLayerScope.() -> Unit),
) : ModifierNodeElement<LayoutAnimationModifierNode>() {

    override fun create() = LayoutAnimationModifierNode(
        layoutAnimations = layoutAnimations,
        graphicsLayerBlock = graphicsLayerBlock,
    )

    override fun update(node: LayoutAnimationModifierNode) {
        node.layoutAnimations = layoutAnimations
        node.graphicsLayerBlock = graphicsLayerBlock
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "layoutAnimation"
    }
}

// We apply every transition when the transition is first transition of its type
// Or the animation has already started, but in cases that the transition is actually
// first transition of its type but it comes after a sequence, we must ignore it until
// it starts. so for simplicity we just change the type of transitions which run
// sequentially to this specific type to be ignored.
// Example: scale() then fade(), fade must not start immediately
private const val SequenceType = "#sequence"