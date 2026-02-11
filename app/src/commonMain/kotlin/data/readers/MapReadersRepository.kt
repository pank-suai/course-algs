package data.readers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import reader.Item
import reader.ReaderHashTable
import reader.models.Reader
import reader.models.ReaderTicket


/**
 * Репозиторий читателей, который используют разработанную хэш-таблицу для хранения
 */
class MapReadersRepository: ReadersRepository {
    private var readersHashTable = ReaderHashTable()


    private val readerList = MutableStateFlow<Array<Reader>>(arrayOf())

    override val readersList: Flow<Array<Reader>> = readerList

    override fun getReaderByTicket(readerTicket: ReaderTicket): Reader? = readersHashTable.findByTicket(readerTicket)

    override fun upsertReader(reader: Reader) {
        readersHashTable.put(Item(reader.readerTicket, reader))
        updateReaderList()
    }

    override fun removeReader(reader: Reader) = removeReader(reader.readerTicket)

    override fun removeReader(readerTicket: ReaderTicket) {
        readersHashTable.remove(readerTicket)
        updateReaderList()
    }

    override fun clear() {
        readersHashTable.clear()
        readerList.value = readersHashTable.toArray()
    }

    private fun updateReaderList() {
        readerList.value = readersHashTable.toArray()
    }
}