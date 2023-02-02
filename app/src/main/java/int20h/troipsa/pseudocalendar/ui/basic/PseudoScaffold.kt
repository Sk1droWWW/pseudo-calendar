package int20h.troipsa.pseudocalendar.ui.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import int20h.troipsa.pseudocalendar.ui.theme.PseudocalendarTheme

@Composable
fun PseudoScaffold(
    modifier: Modifier = Modifier,
    fullScreenProgress: Boolean = false,
    topBar: @Composable () -> Unit = { },
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colors.background,
    contentColor: Color = contentColorFor(containerColor),
    content: @Composable (PaddingValues) -> Unit
) {
    PseudocalendarTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                modifier = modifier.fillMaxSize(),
                topBar = topBar,
                floatingActionButton = floatingActionButton,
                floatingActionButtonPosition = floatingActionButtonPosition,
                contentColor = contentColor,
                content = content,
            )
            if (fullScreenProgress) {
                FullScreenProgress()
            }
        }
    }
}

@Composable
private fun FullScreenProgress() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(MaterialTheme.colors.surface.copy(alpha = 0.5f))
            .fillMaxSize()
    ) {
        CircularProgressIndicator(color = MaterialTheme.colors.primary)
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PseudocalendarTheme {

    }
}