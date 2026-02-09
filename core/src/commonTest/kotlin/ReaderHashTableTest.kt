import reader.*
import reader.models.*
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class ReaderHashTableTest {

    val readers = listOf(
        ReaderTicket("А1234-20") to Reader(ReaderTicket("А1234-20"), "Иванов Иван Иванович", 1990, "ул. Ленина, д. 1", "ООО Ромашка"),
        ReaderTicket("Ч5678-21") to Reader(ReaderTicket("Ч5678-21"), "Петров Петр Петрович", 1985, "ул. Пушкина, д. 2", "ГБУЗ Городская больница №1"),
        ReaderTicket("В9012-22") to Reader(ReaderTicket("В9012-22"), "Сидоров Сидор Сидорович", 2000, "ул. Гагарина, д. 3", "МГУ")
    )

    @Test
    fun addValuesToHashTable() {
        val hashTable = ReaderHashTable()

        readers.forEach { reader ->
            hashTable.put(reader)
        }

        readers.forEach { (ticket, reader) ->
            val foundReader = hashTable.findByTicket(ticket)
            assertEquals(reader, foundReader)
        }
    }

    @Test
    fun collisionHandling() {
        val hashTable = ReaderHashTable(capacity = 2)

        readers.forEach { reader ->
            hashTable.put(reader)
        }

        readers.forEach { (ticket, reader) ->
            val foundReader = hashTable.findByTicket(ticket)
            assertEquals(reader, foundReader)
        }
    }

    @Test
    fun removeValueFromHashTable() {
        val hashTable = ReaderHashTable()

        readers.forEach { reader ->
            hashTable.put(reader)
        }

        val ticketToRemove = readers[1].key
        hashTable.remove(ticketToRemove)

        assertEquals(null, hashTable.findByTicket(ticketToRemove))
    }

    @Test
    fun containsOperator() {
        val hashTable = ReaderHashTable()

        readers.forEach { reader ->
            hashTable.put(reader)
        }

        val existingTicket = readers[0].key
        val nonExistingTicket = ReaderTicket("А0000-00")

        assertEquals(true, existingTicket in hashTable)
        assertEquals(false, nonExistingTicket in hashTable)
    }

    @Test
    fun toListTest() {
        val hashTable = ReaderHashTable()

        readers.forEach { reader ->
            hashTable.put(reader)
        }

        val itemList = hashTable.toArray()
        assertEquals(readers.size, itemList.size)

    }
}