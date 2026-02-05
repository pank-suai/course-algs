import reader.models.AccessType
import reader.models.ReaderTicket
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class ReaderTicketTest {

    @Test
    fun correctlyReaderTicket() {
        val ticket = ReaderTicket("А1234-20")
        assertEquals(ticket.accessType, AccessType.SUBSCRIPTION)
        assertEquals(ticket.number, 1234)
        assertEquals(ticket.year, 2020)
    }

    @Test
    fun incorrectlyReaderTicket() {
        val badTickets = listOf(
            "X1234-20", // неверный тип доступа
            "А123-20",  // номер билета меньше 4 цифр
            "А12345-20", // номер билета больше 4 цифр
            "А1234-2",   // год выдачи меньше 2 цифр
            "А1234-2020" // год выдачи больше 2 цифр
        )
        for (badTicket in badTickets) {
            assertFails {
                ReaderTicket(badTicket)
            }
        }
    }

    @Test
    fun accessTypeFromChar() {
        assertEquals(AccessType.SUBSCRIPTION, AccessType.fromChar('А'))
        assertEquals(AccessType.READ_ZONE, AccessType.fromChar('Ч'))
        assertEquals(AccessType.BOTH, AccessType.fromChar('В'))
    }
}