import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.aghajari.composelayoutanimation.TestScreen

fun main() = application {
    Window(
        state = rememberWindowState(
            width = 500.dp,
            height = 720.dp
        ),
        onCloseRequest = ::exitApplication,
        title = "Test screen",
    ) {
        TestScreen()
    }
}