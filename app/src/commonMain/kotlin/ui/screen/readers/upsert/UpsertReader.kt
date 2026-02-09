package ui.screen.readers.upsert

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Экран добавления/обновления читателя
 */
@Composable
fun UpsertReader(modifier: Modifier = Modifier, selectedReaderTicket: String? = null, onUpsertReader: () -> Unit) {
    val isAdding by remember {
        derivedStateOf { selectedReaderTicket == null }
    }

    val viewModel = viewModel {
        UpsertReaderViewModel(selectedReaderTicket)
    }

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            if (isAdding) "Добавление нового читателя" else "Редактирование читателя",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            viewModel.readerTicket,
            { viewModel.readerTicket = it },
            label = { Text("Номер читательского билета") },
            enabled = isAdding
        )
        OutlinedTextField(
            viewModel.fullName,
            { viewModel.fullName = it },
            label = { Text("ФИО") }
        )
        OutlinedTextField(
            viewModel.yearOfBirthday,
            { viewModel.yearOfBirthday = it },
            label = { Text("Год рождения") }
        )
        OutlinedTextField(
            viewModel.address,
            { viewModel.address = it },
            label = { Text("Адрес") }
        )
        OutlinedTextField(
            viewModel.placeOfWork,
            { viewModel.placeOfWork = it },
            label = { Text("Место работы") }
        )

        Text(
            viewModel.errorMessage ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
        Button(onClick = {
            val isUpserted = viewModel.addReader()
            if (isUpserted) onUpsertReader()
        }) {
            Text(if (isAdding) "Добавить читателя" else "Обновить читателя")
        }

    }


}