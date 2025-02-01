import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.aghajari.composelayoutanimation.LayoutAnimationSpec
import com.aghajari.composelayoutanimation.TestScreen
import com.aghajari.composelayoutanimation.then
import com.aghajari.composelayoutanimation.with
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        TestScreen()
    }
}