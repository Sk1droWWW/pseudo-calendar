package int20h.troipsa.pseudocalendar.ui.main


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import droidninja.filepicker.FilePickerConst
import int20h.troipsa.pseudocalendar.ui.navigation.PseudoCalendarNavHost
import int20h.troipsa.pseudocalendar.ui.theme.PseudocalendarTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.generateEvents()

        setContent {
            PseudocalendarTheme {
                PseudoCalendarNavHost()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FilePickerConst.REQUEST_CODE_PHOTO -> if (resultCode == Activity.RESULT_OK && data != null) {
                val uriList = data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_MEDIA)
                if (uriList != null) {
                    viewModel.setFiles(uriList.map { it.toString() })
                }
            }
            FilePickerConst.REQUEST_CODE_DOC -> if (resultCode == Activity.RESULT_OK && data != null) {
                val uriList = data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_DOCS)
                if (uriList != null) {
                    viewModel.setFiles(uriList.map { it.toString() })
                }
            }
        }
    }
}


