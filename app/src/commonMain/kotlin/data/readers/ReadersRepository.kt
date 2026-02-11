package data.readers

import kotlinx.coroutines.flow.Flow
import reader.models.Reader
import reader.models.ReaderTicket

/**
 * Репозиторий для работы с данными о читателях
 */
interface ReadersRepository {

    /**
     * Список всех читателей
     *
     * Поток, который обновляется при изменении данных
     */
    val readersList: Flow<Array<Reader>>

    /**
     * Получить читателя по номеру читательского билета
     */
    fun getReaderByTicket(readerTicket: ReaderTicket): Reader?

    /**
     * Поиск читателей по фрагменту ФИО (с использованием алгоритма Бойера-Мура)
     */
    fun searchByFullName(fullNameFragment: String): List<Reader>

    /**
     * Добавить или изменить
     */
    fun upsertReader(reader: Reader)


    /**
     * Удалить читателя
     */
    fun removeReader(reader: Reader)

    fun removeReader(readerTicket: ReaderTicket)

    fun clear()


}