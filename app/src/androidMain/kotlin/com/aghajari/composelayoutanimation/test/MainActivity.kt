package com.aghajari.composelayoutanimation.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aghajari.composelayoutanimation.LayoutAnimationSpec
import com.aghajari.composelayoutanimation.TestScreen
import com.aghajari.composelayoutanimation.elegantEntranceAnimationSpec
import com.aghajari.composelayoutanimation.test.ui.theme.ComposeLayoutAnimationTheme
import com.aghajari.composelayoutanimation.then
import com.aghajari.composelayoutanimation.with

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeLayoutAnimationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TestScreen(
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun CustomAnimationSpecPreview() {
    ComposeLayoutAnimationTheme {
        TestScreen(
            animationSpec = LayoutAnimationSpec {
                fade(from = 0f, to = 1f) with
                        translationY(from = 0f, to = 100f) then
                        translationY(from = 100f, to = 0f)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ElegantEntranceAnimationSpecPreview() {
    ComposeLayoutAnimationTheme {
        TestScreen(
            animationSpec = elegantEntranceAnimationSpec(),
        )
    }
}