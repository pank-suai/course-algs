package ui.screen.loans

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import book.models.Book
import book.models.BookCipher
import data.books.BooksRepository
import data.books.BooksRepositorySingleton
import data.loans.LoansRepository
import data.loans.LoansRepositorySingleton
import data.readers.ReadersRepository
import data.readers.ReadersRepositorySingleton
import kotlinx.coroutines.flow.*
import loan.models.Loan
import reader.models.Reader
import reader.models.ReaderTicket
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class LoansViewModel(
    private val loansRepository: LoansRepository = LoansRepositorySingleton.loansRepository,
    private val booksRepository: BooksRepository = BooksRepositorySingleton.booksRepository,
    private val readersRepository: ReadersRepository = ReadersRepositorySingleton.readersRepository
) : ViewModel() {

    var errorMessage: String? by mutableStateOf(null)

    val loans = loansRepository.loansList
        .stateIn(viewModelScope, SharingStarted.Eagerly, arrayOf())

    val books = booksRepository.booksList
        .stateIn(viewModelScope, SharingStarted.Eagerly, arrayOf())

    val readers = readersRepository.readersList
        .stateIn(viewModelScope, SharingStarted.Eagerly, arrayOf())

    // Поля формы выдачи
    var readerQuery: String by mutableStateOf("")
    var bookQuery: String by mutableStateOf("")
    
    // Выбранные объекты
    var selectedReader: Reader? by mutableStateOf(null)
    var selectedBook: Book? by mutableStateOf(null)

    /**
     * Фильтрация читателей по запросу (ФИО или номер билета)
     * Использует алгоритм Бойера-Мура для поиска по ФИО
     */
    fun filterReaders(query: String): List<Reader> {
        if (query.isBlank()) return readers.value.toList()
        
        // Поиск по ФИО через репозиторий (использует Boyer-Moore)
        val byFullName = readersRepository.searchByFullName(query)
        
        // Поиск по номеру билета
        val lowerQuery = query.lowercase()
        val byTicket = readers.value.filter { reader ->
            reader.readerTicket.value.lowercase().contains(lowerQuery)
        }
        
        // Объединяем результаты без дубликатов
        return (byFullName + byTicket).distinctBy { it.readerTicket.value }
    }

    /**
     * Фильтрация книг по запросу (название, автор или шифр)
     * Показываем только книги с доступными экземплярами
     */
    fun filterBooks(query: String): List<Book> {
        val availableBooks = books.value.filter { it.availableCopies > 0 }
        if (query.isBlank()) return availableBooks
        val lowerQuery = query.lowercase()
        return availableBooks.filter { book ->
            book.name.lowercase().contains(lowerQuery) ||
            book.authors.lowercase().contains(lowerQuery) ||
            book.cipher.value.lowercase().contains(lowerQuery)
        }
    }

    /**
     * Выбор читателя
     */
    fun selectReader(reader: Reader) {
        selectedReader = reader
        readerQuery = "${reader.fullName} (${reader.readerTicket.value})"
    }

    /**
     * Выбор книги
     */
    fun selectBook(book: Book) {
        selectedBook = book
        bookQuery = "${book.name} — ${book.authors} (${book.cipher.value})"
    }

    /**
     * Очистка формы
     */
    fun clearForm() {
        readerQuery = ""
        bookQuery = ""
        selectedReader = null
        selectedBook = null
        errorMessage = null
    }

    /**
     * Выдача книги читателю
     */
    @OptIn(ExperimentalTime::class)
    fun issueBook(): Boolean {
        errorMessage = null

        // Проверка выбора читателя
        val reader = selectedReader
        if (reader == null) {
            errorMessage = "Выберите читателя из списка"
            return false
        }

        // Проверка выбора книги
        val book = selectedBook
        if (book == null) {
            errorMessage = "Выберите книгу из списка"
            return false
        }

        // Проверка наличия экземпляров (на случай если данные изменились)
        val currentBook = booksRepository.getBookByCipher(book.cipher)
        if (currentBook == null || currentBook.availableCopies <= 0) {
            errorMessage = "Нет доступных экземпляров этой книги"
            return false
        }

        // Создание записи о выдаче
        val loan = Loan(
            bookCipher = book.cipher,
            readerTicket = reader.readerTicket,
            issueDate = Clock.System.now()
        )

        loansRepository.issueLoan(loan)

        // Уменьшение количества доступных экземпляров
        val updatedBook = currentBook.copy(availableCopies = currentBook.availableCopies - 1)
        booksRepository.upsertBook(updatedBook)

        // Очистка формы
        clearForm()

        return true
    }

    /**
     * Возврат книги от читателя
     */
    @OptIn(ExperimentalTime::class)
    fun returnBook(loan: Loan): Boolean {
        errorMessage = null

        if (loan.returnDate != null) {
            errorMessage = "Книга уже была возвращена"
            return false
        }

        // Получаем книгу для обновления количества экземпляров
        val book = booksRepository.getBookByCipher(loan.bookCipher)
        if (book == null) {
            errorMessage = "Книга не найдена в системе"
            return false
        }

        // Создаем запись с датой возврата
        val returnedLoan = loan.copy(returnDate = Clock.System.now())
        
        if (!loansRepository.returnLoan(loan, returnedLoan)) {
            errorMessage = "Ошибка при регистрации возврата"
            return false
        }

        // Увеличиваем количество доступных экземпляров
        val updatedBook = book.copy(availableCopies = book.availableCopies + 1)
        booksRepository.upsertBook(updatedBook)

        return true
    }

    /**
     * Получить информацию о книге по шифру
     */
    fun getBookInfo(cipher: BookCipher): Book? = booksRepository.getBookByCipher(cipher)

    /**
     * Получить информацию о читателе по билету
     */
    fun getReaderInfo(ticket: ReaderTicket): Reader? = readersRepository.getReaderByTicket(ticket)

    /**
     * Получить активные выдачи по читателю
     */
    fun getActiveLoansForReader(ticket: ReaderTicket): Array<Loan> = 
        loansRepository.getActiveByReaderTicket(ticket)

    /**
     * Получить активные выдачи по книге
     */
    fun getActiveLoansForBook(cipher: BookCipher): Array<Loan> = 
        loansRepository.getActiveByBookCipher(cipher)
}
