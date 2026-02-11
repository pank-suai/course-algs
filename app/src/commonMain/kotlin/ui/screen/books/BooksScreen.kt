package ui.screen.books

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import book.models.Book
import book.models.BookCipher
import com.composables.add
import ui.icons.search
import ui.screen.books.upsert.UpsertBook
import ui.theme.AppTheme
import kotlin.math.absoluteValue


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BooksScreen() {
    val viewModel = viewModel { BooksViewModel() }

    val books by viewModel.books.collectAsState()

    var isDialogVisible by remember {
        mutableStateOf(false)
    }
    var selectedBookCipher by remember {
        mutableStateOf<String?>(null)
    }

    val searchBarState = rememberSearchBarState()
    val query by viewModel.query.collectAsState()



    Scaffold(
        topBar = {
            var isSearchVisible by remember {
                mutableStateOf(false)
            }
            var menuExpanded by remember {
                mutableStateOf(false)
            }
            CenterAlignedTopAppBar(
                {
                    AnimatedVisibility(!isSearchVisible) {
                        Text("Книги")
                    }
                    AnimatedVisibility(isSearchVisible) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            SearchBar(searchBarState, {
                                SearchBarDefaults.InputField(
                                    query = query,
                                    onQueryChange = viewModel::updateQuery,
                                    onSearch = {},
                                    expanded = false,
                                    onExpandedChange = {}
                                )

                            })
                            Spacer(Modifier.width(10.dp))
                            ExposedDropdownMenuBox(menuExpanded, { menuExpanded = it }) {
                                InputChip(
                                    menuExpanded,
                                    { menuExpanded = !menuExpanded },
                                    label = {
                                        Text(
                                            viewModel.searchBy.title,
                                            maxLines = 1,
                                            modifier = Modifier.basicMarquee()
                                        )
                                    },
                                    modifier = Modifier.width(150.dp)
                                )
                                ExposedDropdownMenu(
                                    modifier = Modifier.heightIn(max = 280.dp),
                                    expanded = menuExpanded,
                                    onDismissRequest = { menuExpanded = true },
                                ) {
                                    SearchBy.entries.forEachIndexed { index, option ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    option.title,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    maxLines = 1,
                                                    modifier = Modifier.basicMarquee()
                                                )
                                            },
                                            onClick = {
                                                viewModel.searchBy = option
                                                menuExpanded = false
                                            },
                                            shape = when (index) {
                                                0 -> MenuDefaults.leadingItemShape
                                                SearchBy.entries.size - 1 -> MenuDefaults.trailingItemShape
                                                else -> MenuDefaults.middleItemShape
                                            },
                                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                        )
                                    }
                                }
                            }

                        }
                    }
                },
                actions = {
                    IconButton(onClick = {
                        isSearchVisible = !isSearchVisible
                    }) {
                        Icon(search, "Поиск книги")
                    }
                })
        },
        floatingActionButton = {
            TooltipBox(
                positionProvider =
                    TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                tooltip = { PlainTooltip { Text("Добавить книгу") } },
                state = rememberTooltipState(),
            ) {
                FloatingActionButton(onClick = {
                    selectedBookCipher = null
                    isDialogVisible = true
                }) {
                    Icon(add, "Добавить книгу")
                }
            }
        }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(140.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp).padding(it),

            ) {
            items(books) { book ->
                BookItem(
                    name = book.name,
                    authors = book.authors,
                    cipher = book.cipher.value,
                    availableCopies = book.availableCopies,
                    totalCopies = book.totalCopies,
                    onClick = {
                        selectedBookCipher = book.cipher.value
                        isDialogVisible = true
                    }
                )
            }
        }
    }
    AnimatedVisibility(isDialogVisible) {
        Dialog(onDismissRequest = {
            isDialogVisible = false
        }) {
            Surface(shape = MaterialTheme.shapes.large, tonalElevation = 6.dp) {
                UpsertBook(Modifier.padding(10.dp), selectedBookCipher) {
                    isDialogVisible = false
                }
            }
        }
    }
}

/**
 * Генерирует цвет обложки книги на основе названия
 */
@Composable
private fun generateBookColor(name: String): Color {
    val hash = name.hashCode().absoluteValue
    val hue = (hash % 360).toFloat()
    val saturation = 0.4f + (hash % 30) / 100f
    val lightness = 0.35f + (hash % 20) / 100f
    
    // Конвертация HSL в RGB
    val c = (1 - kotlin.math.abs(2 * lightness - 1)) * saturation
    val x = c * (1 - kotlin.math.abs((hue / 60) % 2 - 1))
    val m = lightness - c / 2
    
    val (r, g, b) = when {
        hue < 60 -> Triple(c, x, 0f)
        hue < 120 -> Triple(x, c, 0f)
        hue < 180 -> Triple(0f, c, x)
        hue < 240 -> Triple(0f, x, c)
        hue < 300 -> Triple(x, 0f, c)
        else -> Triple(c, 0f, x)
    }
    
    return Color(r + m, g + m, b + m)
}

/**
 * Компонент книги, стилизованный под реальную книгу с обложкой
 */
@Composable
fun BookItem(
    name: String,
    authors: String,
    cipher: String,
    availableCopies: Int,
    totalCopies: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bookColor = generateBookColor(name)
    val darkerColor = bookColor.copy(
        red = (bookColor.red * 0.7f).coerceIn(0f, 1f),
        green = (bookColor.green * 0.7f).coerceIn(0f, 1f),
        blue = (bookColor.blue * 0.7f).coerceIn(0f, 1f)
    )
    val lighterColor = bookColor.copy(
        red = (bookColor.red * 1.2f).coerceIn(0f, 1f),
        green = (bookColor.green * 1.2f).coerceIn(0f, 1f),
        blue = (bookColor.blue * 1.2f).coerceIn(0f, 1f)
    )

    Box(
        modifier = modifier
            .width(140.dp)
            .height(200.dp)
            .clickable(onClick = onClick)
    ) {
        // Тень книги
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = 4.dp, y = 4.dp)
                .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                .background(Color.Black.copy(alpha = 0.2f))
        )

        // Корешок книги (левая часть)
        Box(
            modifier = Modifier
                .width(12.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 2.dp, bottomStart = 2.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(darkerColor, bookColor)
                    )
                )
                .shadow(2.dp)
        )

        // Основная обложка книги
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp)
                .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(lighterColor, bookColor, darkerColor)
                    )
                )
        ) {
            // Декоративная рамка на обложке
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.White.copy(alpha = 0.1f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Шифр книги (сверху)
                Text(
                    text = cipher,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )

                // Название книги (по центру)
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f).padding(vertical = 8.dp),
                    lineHeight = 16.sp
                )

                // Автор (снизу)
                Text(
                    text = authors,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(4.dp))

                // Количество экземпляров
                Surface(
                    color = if (availableCopies > 0) 
                        Color.White.copy(alpha = 0.25f) 
                    else 
                        Color.Red.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "$availableCopies / $totalCopies",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }

        // Эффект блика на обложке
        Box(
            modifier = Modifier
                .width(20.dp)
                .fillMaxHeight()
                .padding(start = 12.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}

@Preview
@Composable
fun BookItemPreview() {
    AppTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            BookItem(
                name = "Война и мир",
                authors = "Л.Н. Толстой",
                cipher = "001.001",
                availableCopies = 3,
                totalCopies = 5,
                onClick = {}
            )
            BookItem(
                name = "Преступление и наказание",
                authors = "Ф.М. Достоевский",
                cipher = "001.002",
                availableCopies = 0,
                totalCopies = 2,
                onClick = {}
            )
            BookItem(
                name = "Мастер и Маргарита",
                authors = "М.А. Булгаков",
                cipher = "002.001",
                availableCopies = 1,
                totalCopies = 1,
                onClick = {}
            )
        }
    }
}
