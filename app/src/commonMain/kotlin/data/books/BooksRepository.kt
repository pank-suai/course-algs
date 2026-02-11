package data.books

import book.models.Book
import book.models.BookCipher
import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий для работы с данными о книгах
 */
interface BooksRepository {

    /**
     * Список всех книг
     *
     * Поток, который обновляется при изменении данных
     */
    val booksList: Flow<Array<Book>>

    /**
     * Получить книгу по шифру
     */
    fun getBookByCipher(cipher: BookCipher): Book?

    /**
     * Добавить или изменить книгу
     */
    fun upsertBook(book: Book)

    /**
     * Удалить книгу
     */
    fun removeBook(book: Book)

    fun removeBook(cipher: BookCipher)

    fun clear()
}
