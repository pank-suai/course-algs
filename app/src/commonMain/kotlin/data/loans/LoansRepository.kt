package data.loans

import book.models.BookCipher
import kotlinx.coroutines.flow.Flow
import loan.models.Loan
import reader.models.ReaderTicket

/**
 * Репозиторий для работы с данными о выдаче книг
 */
interface LoansRepository {

    /**
     * Список всех записей о выдаче
     *
     * Поток, который обновляется при изменении данных
     */
    val loansList: Flow<Array<Loan>>

    /**
     * Получить записи о выдаче по шифру книги
     */
    fun getLoansByBookCipher(cipher: BookCipher): Array<Loan>

    /**
     * Получить записи о выдаче по номеру читательского билета
     */
    fun getLoansByReaderTicket(readerTicket: ReaderTicket): Array<Loan>

    /**
     * Получить активные (невозвращенные) записи по шифру книги
     */
    fun getActiveByBookCipher(cipher: BookCipher): Array<Loan>

    /**
     * Получить активные (невозвращенные) записи по номеру читательского билета
     */
    fun getActiveByReaderTicket(readerTicket: ReaderTicket): Array<Loan>

    /**
     * Проверить наличие активных выдач для читателя
     */
    fun hasActiveLoansForReader(readerTicket: ReaderTicket): Boolean

    /**
     * Проверить наличие активных выдач для книги
     */
    fun hasActiveLoansForBook(cipher: BookCipher): Boolean

    /**
     * Зарегистрировать выдачу книги
     */
    fun issueLoan(loan: Loan)

    /**
     * Зарегистрировать возврат книги
     */
    fun returnLoan(loan: Loan, returnedLoan: Loan): Boolean

    /**
     * Удалить запись о выдаче
     */
    fun removeLoan(loan: Loan): Boolean

    /**
     * Очистить все записи
     */
    fun clear()
}
