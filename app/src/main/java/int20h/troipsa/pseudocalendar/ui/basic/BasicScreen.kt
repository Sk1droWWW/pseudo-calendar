package int20h.troipsa.pseudocalendar.ui.basic

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import int20h.troipsa.pseudocalendar.R
import int20h.troipsa.pseudocalendar.ui.theme.PseudocalendarTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PseudoScaffold(
    modifier: Modifier = Modifier,
    fullScreenProgress: Boolean = false,
    topBar: @Composable () -> Unit = defaultTopBarProvider(),
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

fun defaultTopBarProvider(
    title: String = "",
    closeIcon: Painter? = null,
    actionButton: @Composable (() -> Unit)? = null,
) = @Composable {
    TopBar(
        header = title,
        homeButton = { ButtonBack(icon = closeIcon) },
        actionButton = actionButton
    )
}

fun homeTopBarProvider(
    title: String = "",
    actionButton: @Composable (() -> Unit)? = null,
) = @Composable {
    TopBar(
        header = title,
        homeButton = { },
        actionButton = actionButton
    )
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

@Composable
fun TopBar(
    header: String,
    homeButton: @Composable (() -> Unit)? = { ButtonBack(icon = null) },
    actionButton: @Composable (() -> Unit)? = null
) {
    TopAppBar(
        modifier = Modifier.statusBarsPadding(),
        contentPadding = PaddingValues(end = 16.dp),
        backgroundColor = MaterialTheme.colors.primary,
        elevation = 0.dp
    ) {
        CompositionLocalProvider(LocalContentAlpha provides 1f) {
            if (homeButton != null) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    homeButton()
                }
            } else {
                Spacer(modifier = Modifier.width(16.dp))
            }

            Text(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f),
                text = header,
                style = MaterialTheme.typography.h5,
                color = Color.White,
                maxLines = 2,
            )
            if (actionButton != null) {
                Box(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    actionButton()
                }
            }
        }
    }
}

@Composable
fun ButtonBack(
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    tint: Color = LocalContentColor.current
) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    Icon(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = { backDispatcher?.onBackPressed() })
            .padding(16.dp),
        painter = icon ?: painterResource(id = R.drawable.ic_back_arrow),
        contentDescription = null,
        tint = tint,
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PseudocalendarTheme {

    }
}