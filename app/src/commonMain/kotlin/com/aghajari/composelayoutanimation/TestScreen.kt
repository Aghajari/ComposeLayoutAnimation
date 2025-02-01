package com.aghajari.composelayoutanimation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TestScreen(
    modifier: Modifier = Modifier,
    animationSpec: LayoutAnimationSpec =
        elegantEntranceAnimationSpec(),
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        LayoutAnimation(animationSpec = animationSpec) {
            repeat(5) { index ->
                CardItem(
                    text = "Item $index",
                    modifier = Modifier.animateLayoutItem(),
                )
            }
        }
    }
}

@Composable
fun CardItem(
    text: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
        )
    }
}

