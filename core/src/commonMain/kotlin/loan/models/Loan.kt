package loan.models

import book.models.BookCipher
import reader.models.ReaderTicket
import kotlin.time.Instant


data class Loan(
    val bookCipher: BookCipher,
    val readerTicket: ReaderTicket,
    val issueDate: Instant,
    val returnDate: Instant? = null
): Comparable<Loan> {
    init {
        require(returnDate == null || returnDate >= issueDate) // дата возврата не может быть раньше даты выдачи
    }

    override fun compareTo(other: Loan): Int = this.bookCipher.compareTo(other.bookCipher)
}
