package ui.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SettingsScreen() {

    val viewModel = viewModel { SettingsViewModel() }
    Column {
        Button(onClick = viewModel::fillData) {
            Text("Импортировать тестовые данные")
        }
        Button(
            onClick = viewModel::clearAllData, colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text("Очистить")
        }
    }
}