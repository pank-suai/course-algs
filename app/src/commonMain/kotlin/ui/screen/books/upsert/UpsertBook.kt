package ui.screen.books.upsert

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Экран добавления/обновления книги
 */
@Composable
fun UpsertBook(modifier: Modifier = Modifier, selectedBookCipher: String? = null, onUpsertBook: () -> Unit) {
    val isAdding by remember {
        derivedStateOf { selectedBookCipher == null }
    }

    val viewModel = viewModel(key = selectedBookCipher) {
        UpsertBookViewModel(selectedBookCipher)
    }

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            if (isAdding) "Добавление новой книги" else "Редактирование книги",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            viewModel.cipher,
            { viewModel.cipher = it },
            label = { Text("Шифр книги (NNN.MMM)") },
            enabled = isAdding,
            placeholder = { Text("001.001") }
        )
        
        OutlinedTextField(
            viewModel.name,
            { viewModel.name = it },
            label = { Text("Название") }
        )
        
        OutlinedTextField(
            viewModel.authors,
            { viewModel.authors = it },
            label = { Text("Авторы") }
        )
        
        OutlinedTextField(
            viewModel.publishing,
            { viewModel.publishing = it },
            label = { Text("Издательство") }
        )
        
        OutlinedTextField(
            viewModel.yearOfPublishing,
            { viewModel.yearOfPublishing = it },
            label = { Text("Год издания") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        
        Row {
            OutlinedTextField(
                viewModel.totalCopies,
                { viewModel.totalCopies = it },
                label = { Text("Всего экз.") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            OutlinedTextField(
                viewModel.availableCopies,
                { viewModel.availableCopies = it },
                label = { Text("Доступно") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        // Информация о выданных экземплярах
        if (!isAdding && viewModel.activeLoansCount > 0) {
            Spacer(Modifier.height(8.dp))
            Surface(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    "Выдано экземпляров: ${viewModel.activeLoansCount}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
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
                    val removed = viewModel.removeBook()
                    if (removed) onUpsertBook()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Списать книгу")
            }
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                val isUpserted = viewModel.addBook()
                if (isUpserted) onUpsertBook()
            }) {
                Text(if (isAdding) "Добавить книгу" else "Обновить книгу")
            }
        }

    }
}
