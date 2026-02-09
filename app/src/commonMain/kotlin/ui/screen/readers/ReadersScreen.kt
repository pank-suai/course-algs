package ui.screen.readers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composables.add
import ui.screen.readers.upsert.UpsertReader
import ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadersScreen() {
    val viewModel = viewModel { ReadersViewModel() }

    val readers by viewModel.readers.collectAsState()

    var isDialogVisible by remember {
        mutableStateOf(false)
    }
    var selectedReaderTicket by remember {
        mutableStateOf<String?>(null)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar({
                Text("Читатели")
            })
        },
        floatingActionButton = {
            TooltipBox(
                positionProvider =
                    TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                tooltip = { PlainTooltip { Text("Добавить читателя") } },
                state = rememberTooltipState(),
            ) {
                FloatingActionButton(onClick = {
                    isDialogVisible = true
                }) {
                    Icon(add, "Добавить читателя")
                }
            }
        }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(200.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(10.dp).padding(it),

            ) {
            items(readers) { reader ->
                ReaderItem(reader.readerTicket.value, reader.fullName, reader.yearOfBirthday, {
                    selectedReaderTicket = reader.readerTicket.value
                    isDialogVisible = true
                })
            }
        }
    }
    AnimatedVisibility(isDialogVisible) {
        Dialog(onDismissRequest = {
            isDialogVisible = false
            selectedReaderTicket = null
        }) {
            Surface(shape = MaterialTheme.shapes.large, tonalElevation = 6.dp) {
                UpsertReader(Modifier.padding(10.dp), selectedReaderTicket) {
                    isDialogVisible = false
                }
            }
        }
    }
}


@Composable
fun ReaderItem(
    id: String,
    fullName: String,
    yearOfBirthday: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(onClick = onClick, modifier) {
        Column(modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)) {
            Row {
                Text(fullName, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

            }
            Spacer(Modifier.height(4.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(id)
                Text("$yearOfBirthday", maxLines = 1)
            }

        }
    }
}

@Preview
@Composable
fun ReaderItemPreview() {
    AppTheme {
        ReaderItem("А1234-20", "Иванов Иван Иванович", 1990, {})
    }
}