package ui.screen.loans

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import book.models.Book
import com.composables.add
import loan.models.Loan
import reader.models.Reader
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class, ExperimentalTime::class)
@Composable
fun LoansScreen() {
    val viewModel = viewModel { LoansViewModel() }

    val loans by viewModel.loans.collectAsState()

    var isIssueDialogVisible by remember { mutableStateOf(false) }

    // Разделяем на активные и возвращенные
    val activeLoans = loans.filter { it.returnDate == null }
    val returnedLoans = loans.filter { it.returnDate != null }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Выдача книг") }
            )
        },
        floatingActionButton = {
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                tooltip = { PlainTooltip { Text("Выдать книгу") } },
                state = rememberTooltipState(),
            ) {
                FloatingActionButton(onClick = { isIssueDialogVisible = true }) {
                    Icon(add, "Выдать книгу")
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (activeLoans.isNotEmpty()) {
                item {
                    Text(
                        "Активные выдачи (${activeLoans.size})",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(activeLoans) { loan ->
                    LoanCard(
                        loan = loan,
                        bookName = viewModel.getBookInfo(loan.bookCipher)?.name ?: "Неизвестно",
                        readerName = viewModel.getReaderInfo(loan.readerTicket)?.fullName ?: "Неизвестно",
                        isActive = true,
                        onReturn = { viewModel.returnBook(loan) }
                    )
                }
            }

            if (returnedLoans.isNotEmpty()) {
                item {
                    Text(
                        "История возвратов (${returnedLoans.size})",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(returnedLoans.takeLast(20).reversed()) { loan ->
                    LoanCard(
                        loan = loan,
                        bookName = viewModel.getBookInfo(loan.bookCipher)?.name ?: "Неизвестно",
                        readerName = viewModel.getReaderInfo(loan.readerTicket)?.fullName ?: "Неизвестно",
                        isActive = false,
                        onReturn = {}
                    )
                }
            }

            if (loans.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Нет записей о выдаче книг",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    // Диалог выдачи книги
    AnimatedVisibility(isIssueDialogVisible) {
        Dialog(onDismissRequest = { isIssueDialogVisible = false }) {
            Surface(shape = MaterialTheme.shapes.large, tonalElevation = 6.dp) {
                IssueBookDialog(
                    viewModel = viewModel,
                    onDismiss = { isIssueDialogVisible = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun LoanCard(
    loan: Loan,
    bookName: String,
    readerName: String,
    isActive: Boolean,
    onReturn: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        bookName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee()
                    )
                    Text(
                        "Шифр: ${loan.bookCipher.value}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (isActive) {
                    AssistChip(
                        onClick = {},
                        label = { Text("Выдана") },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                } else {
                    AssistChip(
                        onClick = {},
                        label = { Text("Возвращена") },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Читатель: $readerName",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Билет: ${loan.readerTicket.value}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Выдана: ${formatDate(loan.issueDate)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    if (loan.returnDate != null) {
                        Text(
                            "Возвращена: ${formatDate(loan.returnDate!!)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                if (isActive) {
                    Button(
                        onClick = onReturn,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("Принять")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
private fun formatDate(instant: kotlin.time.Instant): String {
    // Простое форматирование даты
    return instant.toString().substringBefore("T")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueBookDialog(
    viewModel: LoansViewModel,
    onDismiss: () -> Unit
) {
    var readerDropdownExpanded by remember { mutableStateOf(false) }
    var bookDropdownExpanded by remember { mutableStateOf(false) }
    
    val filteredReaders = remember(viewModel.readerQuery) { 
        viewModel.filterReaders(viewModel.readerQuery) 
    }
    val filteredBooks = remember(viewModel.bookQuery) { 
        viewModel.filterBooks(viewModel.bookQuery) 
    }

    // Очистка формы при открытии диалога
    LaunchedEffect(Unit) {
        viewModel.clearForm()
    }

    Column(
        modifier = Modifier.padding(16.dp).widthIn(min = 350.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Выдача книги",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(16.dp))

        // Autocomplete для читателя
        ExposedDropdownMenuBox(
            expanded = readerDropdownExpanded && filteredReaders.isNotEmpty(),
            onExpandedChange = { readerDropdownExpanded = it }
        ) {
            OutlinedTextField(
                value = viewModel.readerQuery,
                onValueChange = { 
                    viewModel.readerQuery = it
                    viewModel.selectedReader = null
                    readerDropdownExpanded = true
                },
                label = { Text("Читатель") },
                placeholder = { Text("Начните вводить ФИО или номер билета") },
                modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
                singleLine = true,
                supportingText = if (viewModel.selectedReader != null) {
                    { Text("Выбран: ${viewModel.selectedReader?.readerTicket?.value}") }
                } else null
            )
            
            ExposedDropdownMenu(
                expanded = readerDropdownExpanded && filteredReaders.isNotEmpty() && viewModel.selectedReader == null,
                onDismissRequest = { readerDropdownExpanded = false }
            ) {
                filteredReaders.take(5).forEach { reader ->
                    ReaderDropdownItem(
                        reader = reader,
                        onClick = {
                            viewModel.selectReader(reader)
                            readerDropdownExpanded = false
                        }
                    )
                }
                if (filteredReaders.size > 5) {
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { 
                            Text(
                                "...и ещё ${filteredReaders.size - 5}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ) 
                        },
                        onClick = {},
                        enabled = false
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Autocomplete для книги
        ExposedDropdownMenuBox(
            expanded = bookDropdownExpanded && filteredBooks.isNotEmpty(),
            onExpandedChange = { bookDropdownExpanded = it }
        ) {
            OutlinedTextField(
                value = viewModel.bookQuery,
                onValueChange = { 
                    viewModel.bookQuery = it
                    viewModel.selectedBook = null
                    bookDropdownExpanded = true
                },
                label = { Text("Книга") },
                placeholder = { Text("Начните вводить название, автора или шифр") },
                modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
                singleLine = true,
                supportingText = if (viewModel.selectedBook != null) {
                    { Text("Доступно: ${viewModel.selectedBook?.availableCopies} экз.") }
                } else {
                    { Text("Показаны только книги с доступными экземплярами") }
                }
            )
            
            ExposedDropdownMenu(
                expanded = bookDropdownExpanded && filteredBooks.isNotEmpty() && viewModel.selectedBook == null,
                onDismissRequest = { bookDropdownExpanded = false }
            ) {
                filteredBooks.take(5).forEach { book ->
                    BookDropdownItem(
                        book = book,
                        onClick = {
                            viewModel.selectBook(book)
                            bookDropdownExpanded = false
                        }
                    )
                }
                if (filteredBooks.size > 5) {
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { 
                            Text(
                                "...и ещё ${filteredBooks.size - 5}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ) 
                        },
                        onClick = {},
                        enabled = false
                    )
                }
            }
        }

        if (viewModel.errorMessage != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                viewModel.errorMessage!!,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
        ) {
            TextButton(onClick = {
                viewModel.clearForm()
                onDismiss()
            }) {
                Text("Отмена")
            }
            Button(
                onClick = {
                    if (viewModel.issueBook()) {
                        onDismiss()
                    }
                },
                enabled = viewModel.selectedReader != null && viewModel.selectedBook != null
            ) {
                Text("Выдать")
            }
        }
    }
}

@Composable
private fun ReaderDropdownItem(
    reader: Reader,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Column {
                Text(
                    reader.fullName,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Билет: ${reader.readerTicket.value}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        onClick = onClick
    )
}

@Composable
private fun BookDropdownItem(
    book: Book,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Column {
                Text(
                    book.name,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "${book.authors} | ${book.cipher.value}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "${book.availableCopies} экз.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        onClick = onClick
    )
}
