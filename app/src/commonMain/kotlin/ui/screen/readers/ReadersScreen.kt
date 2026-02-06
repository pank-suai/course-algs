package ui.screen.readers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.composables.add
import ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadersScreen() {
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
                FloatingActionButton(onClick = { /*TODO*/ }) {
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
            items(100) {
                ReaderItemPreview()

            }
        }
    }
}


@Composable
fun ReaderItem(
    id: String,
    fullName: String,
    yearOfBirthday: Int,
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier) {
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
        ReaderItem("А1234-20", "Иванов Иван Иванович", 1990)
    }
}