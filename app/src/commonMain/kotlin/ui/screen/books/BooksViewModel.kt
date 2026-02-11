package ui.screen.books

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import book.models.Book
import book.models.BookCipher
import data.books.BooksRepository
import data.books.BooksRepositorySingleton
import kotlinx.coroutines.flow.*

enum class SearchBy(val title: String) {
    Cipher("Шифр книги"),
    Name("Название"),
    Authors("Авторы")
}

class BooksViewModel(private val booksRepository: BooksRepository = BooksRepositorySingleton.booksRepository) :
    ViewModel() {

    private val _query = MutableStateFlow<String>("")
    var query: StateFlow<String> = _query.asStateFlow()
    var searchBy: SearchBy by mutableStateOf(SearchBy.Name)


    val books = booksRepository.booksList.combine(query) { books, query ->
        if (query.isBlank()) {
            return@combine books
        }
        return@combine when (searchBy) {
            SearchBy.Cipher -> {
                if (BookCipher.validate(query)) {
                    booksRepository.getBookByCipher(BookCipher(query))?.let {
                        arrayOf(it)
                    } ?: arrayOf()
                } else {
                    books.filter { it.cipher.value.contains(query, ignoreCase = true) }.toTypedArray()
                }
            }
            SearchBy.Name -> books.filter { it.name.contains(query, ignoreCase = true) }.toTypedArray()
            SearchBy.Authors -> books.filter { it.authors.contains(query, ignoreCase = true) }.toTypedArray()
        }

    }.stateIn(viewModelScope, SharingStarted.Eagerly, arrayOf())

    fun updateQuery(newQuery: String) {
        _query.value = newQuery
    }

    fun upsertBook(book: Book) {
        booksRepository.upsertBook(book)
    }

    fun removeBook(book: Book) {
        booksRepository.removeBook(book)
    }

}
