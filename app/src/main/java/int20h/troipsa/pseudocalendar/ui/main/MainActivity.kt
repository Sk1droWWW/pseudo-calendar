package int20h.troipsa.pseudocalendar.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import dagger.hilt.android.AndroidEntryPoint
import int20h.troipsa.pseudocalendar.ui.theme.PseudocalendarTheme
import androidx.lifecycle.viewmodel.compose.viewModel


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PseudocalendarTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BasicScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun BasicScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val user by viewModel.user.collectAsState()

    Column(
        modifier = Modifier
            .padding(
                vertical = 16.dp,
                horizontal = 64.dp
            )
            .fillMaxWidth()
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PseudocalendarTheme {
        BasicScreen()
    }
}