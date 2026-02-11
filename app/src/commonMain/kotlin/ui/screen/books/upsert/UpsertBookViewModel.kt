package ui.screen.books.upsert

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import book.models.Book
import book.models.BookCipher
import data.books.BooksRepository
import data.books.BooksRepositorySingleton
import data.loans.LoansRepository
import data.loans.LoansRepositorySingleton
import utils.currentYear

class UpsertBookViewModel(
    selectedBookCipher: String? = null, // Если null, то это добавление новой книги, иначе - редактирование существующей
    private val booksRepository: BooksRepository = BooksRepositorySingleton.booksRepository,
    private val loansRepository: LoansRepository = LoansRepositorySingleton.loansRepository
) : ViewModel() {

    var errorMessage: String? by mutableStateOf(null)

    var cipher: String by mutableStateOf("")
    var name: String by mutableStateOf("")
    var authors: String by mutableStateOf("")
    var publishing: String by mutableStateOf("")
    var yearOfPublishing: String by mutableStateOf("")
    var totalCopies: String by mutableStateOf("")
    var availableCopies: String by mutableStateOf("")

    // Информация о выданных экземплярах
    val activeLoansCount: Int
        get() = if (BookCipher.validate(cipher)) {
            loansRepository.getActiveByBookCipher(BookCipher(cipher)).size
        } else 0

    init {
        if (selectedBookCipher != null) {
            val book = booksRepository.getBookByCipher(BookCipher(selectedBookCipher))
            if (book != null) {
                cipher = book.cipher.value
                name = book.name
                authors = book.authors
                publishing = book.publishing
                yearOfPublishing = book.yearOfPublishing.toString()
                totalCopies = book.totalCopies.toString()
                availableCopies = book.availableCopies.toString()
            }
        }
    }

    fun addBook(): Boolean {
        // Валидация шифра
        if (!BookCipher.validate(cipher)) {
            errorMessage = "Шифр книги должен быть в формате NNN.MMM (например, 001.001)"
            return false
        }

        // Проверка на существование книги с таким шифром (только при добавлении)
        val existingBook = booksRepository.getBookByCipher(BookCipher(cipher))
        if (existingBook != null && name.isBlank()) {
            // Если редактируем существующую книгу - это ок
        }

        // Проверка на пустые поля
        if (name.isBlank() || authors.isBlank() || publishing.isBlank() || 
            yearOfPublishing.isBlank() || totalCopies.isBlank() || availableCopies.isBlank()) {
            errorMessage = "Все поля должны быть заполнены"
            return false
        }

        // Валидация года издания
        val year = yearOfPublishing.toIntOrNull()
        if (year == null || year !in 1450..currentYear()) {
            errorMessage = "Год издания должен быть от 1450 до ${currentYear()}"
            return false
        }

        // Валидация количества экземпляров
        val total = totalCopies.toIntOrNull()
        val available = availableCopies.toIntOrNull()
        if (total == null || total < 0) {
            errorMessage = "Общее количество экземпляров должно быть неотрицательным числом"
            return false
        }
        if (available == null || available < 0 || available > total) {
            errorMessage = "Доступное количество должно быть от 0 до общего количества ($total)"
            return false
        }

        val book = Book(
            cipher = BookCipher(cipher),
            name = name,
            authors = authors,
            publishing = publishing,
            yearOfPublishing = year,
            totalCopies = total,
            availableCopies = available
        )
        booksRepository.upsertBook(book)
        errorMessage = null

        return true
    }

    fun removeBook(): Boolean {
        if (!BookCipher.validate(cipher)) {
            errorMessage = "Невозможно удалить книгу с некорректным шифром"
            return false
        }
        
        // Проверка наличия невозвращенных экземпляров
        val bookCipher = BookCipher(cipher)
        if (loansRepository.hasActiveLoansForBook(bookCipher)) {
            val activeLoans = loansRepository.getActiveByBookCipher(bookCipher)
            errorMessage = "Невозможно списать книгу: ${activeLoans.size} экземпляров на руках у читателей"
            return false
        }
        
        booksRepository.removeBook(bookCipher)
        return true
    }
}
