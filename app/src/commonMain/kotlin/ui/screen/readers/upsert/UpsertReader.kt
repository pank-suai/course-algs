package ui.screen.readers.upsert

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Экран добавления/обновления читателя
 */
@Composable
fun UpsertReader(modifier: Modifier = Modifier, selectedReaderTicket: String? = null, onUpsertReader: () -> Unit) {
    val isAdding by remember {
        derivedStateOf { selectedReaderTicket == null }
    }

    val viewModel = viewModel(key = selectedReaderTicket) {
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

        // Информация о выданных книгах
        if (!isAdding && viewModel.activeLoansCount > 0) {
            Spacer(Modifier.height(8.dp))
            Surface(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    "Выдано книг: ${viewModel.activeLoansCount}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier
                )
            }
        }

        Text(
            viewModel.errorMessage ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )

        Row {
            Button(
                onClick = {
                    val removed = viewModel.removeReader()
                    if (removed) onUpsertReader()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Снять с обслуживания")
            }
            Button(onClick = {
                val isUpserted = viewModel.addReader()
                if (isUpserted) onUpsertReader()
            }) {
                Text(if (isAdding) "Добавить читателя" else "Обновить читателя")
            }
        }

    }


}