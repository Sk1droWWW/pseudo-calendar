package int20h.troipsa.pseudocalendar.ui.basic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import int20h.troipsa.pseudocalendar.ui.main.MainViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import int20h.troipsa.pseudocalendar.ui.theme.PseudocalendarTheme

@Composable
fun BasicScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val user by viewModel.user.collectAsState()

        Column(
            modifier = Modifier
                .padding(
                    vertical = 16.dp,
                    horizontal = 64.dp
                )
                .fillMaxSize()
        ) {
            Text(
                text = "name: ${user?.name ?: ""}",
                fontSize = 14.sp,
            )
            Text(
                text = "phoneNumber: ${user?.phoneNumber ?: ""}",
                fontSize = 14.sp,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PseudocalendarTheme {
        BasicScreen()
    }
}