import book.models.BookCipher
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class BookCipherTest {
    @Test
    fun validCipher() {
        val cipher = BookCipher("123.456")
        assertEquals(cipher.value, "123.456")
        assertEquals(cipher.section, 123)
        assertEquals(cipher.number, 456)
    }

    @Test
    fun invalidCipher() {
        val invalidCiphers = listOf("12.3456", "1234.56", "abc.def", "123456", "123.45a")
        for (invalid in invalidCiphers) {
            assertFails { BookCipher(invalid) }
        }
    }


}