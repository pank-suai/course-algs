import book.models.BookCipher
import loan.LoanSkipList
import loan.models.Loan
import reader.models.ReaderTicket
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class LoanSkipListTest {

    val loans = listOf(
        Loan(
            bookCipher = BookCipher("001.001"),
            readerTicket = ReaderTicket("А0001-25"),
            issueDate = Instant.parse("2025-01-10T10:00:00Z"),
            returnDate = Instant.parse("2025-02-10T14:30:00Z")
        ),
        Loan(
            bookCipher = BookCipher("001.002"),
            readerTicket = ReaderTicket("Ч0002-25"),
            issueDate = Instant.parse("2025-01-15T12:00:00Z"),
            returnDate = null
        ),
        Loan(
            bookCipher = BookCipher("001.001"),
            readerTicket = ReaderTicket("В0003-25"),
            issueDate = Instant.parse("2025-01-20T09:00:00Z"),
            returnDate = Instant.parse("2025-01-30T16:45:00Z")
        ),
        Loan(
            bookCipher = BookCipher("002.005"),
            readerTicket = ReaderTicket("А0001-25"),
            issueDate = Instant.parse("2025-02-01T11:00:00Z"),
            returnDate = null
        )
    )

    @Test
    fun sortedTest() {
        val skipList = LoanSkipList()

        skipList.insert(loans[0]) // 001.001
        skipList.insert(loans[1]) // 001.002
        skipList.insert(loans[2]) // 001.001

        // 001.001, 001.001, 001.002
        assertEquals(skipList.head?.loan?.bookCipher, BookCipher("001.001"))
        assertEquals(skipList.head?.next?.next?.loan?.bookCipher, BookCipher("001.002"))
    }

    @Test
    fun bookGroupingTest() {
        val skipList = LoanSkipList()

        loans.forEach {
            skipList.insert(it)
        }

        // Проверяем группы по шифру книги
        assertEquals(skipList.head?.loan?.bookCipher, BookCipher("001.001"))
        assertEquals(skipList.head?.nextBookGroup?.loan?.bookCipher, BookCipher("001.002"))
        assertEquals(skipList.head?.nextBookGroup?.nextBookGroup?.loan?.bookCipher, BookCipher("002.005"))
    }

    @Test
    fun readerGroupingTest() {
        val skipList = LoanSkipList()

        loans.forEach {
            skipList.insert(it)
        }

        assertEquals(skipList.head?.loan?.readerTicket, ReaderTicket("А0001-25"))
        assertEquals(skipList.head?.nextReaderRecord?.loan?.readerTicket, ReaderTicket("В0003-25"))
        assertEquals(skipList.head?.nextReaderRecord?.nextReaderRecord?.loan?.readerTicket, ReaderTicket("Ч0002-25"))
    }

    @Test
    fun findByBookCipherTest() {
        val skipList = LoanSkipList()

        loans.forEach {
            skipList.insert(it)
        }

        val founded = skipList.findByBookCipher(BookCipher("002.005"))
        assertEquals(founded[0], loans[3])

        val founded2 = skipList.findByBookCipher(BookCipher("001.001"))
        assertEquals(founded2.size, 2)
        assertEquals(founded2[0], loans[0])
        assertEquals(founded2[1], loans[2])
    }

    @Test
    fun findByReaderTicketTest() {
        val skipList = LoanSkipList()

        loans.forEach {
            skipList.insert(it)
        }

        val founded = skipList.findByReaderTicket(ReaderTicket("А0001-25"))
        assertEquals(founded.size, 2)
        assertEquals(founded[0], loans[0])
        assertEquals(founded[1], loans[3])
    }
}