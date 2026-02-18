package reader

import reader.models.Reader
import reader.models.ReaderTicket
import utils.BoyerMoore


data class Item(val key: ReaderTicket, var value: Reader) {
}

infix fun ReaderTicket.to(value: Reader) = Item(this, value)

/**
 * Хеш-таблица для хранения читательских билетов
 *
 * @param capacity размер хеш-таблицы
 */
class ReaderHashTable(val capacity: Int = 300)  // для простоты не будет динамического расширения таблицы
{

    // можно сделать приватным, но для демонстрации оставим открытым
    val table = Array(capacity) { LinearLinkedList<Item>() }


    fun hash(ticket: ReaderTicket): Int = ticket.hash() % capacity

    fun put(item: Item){
        val (key, value) = item
        val index = hash(key)
        val bucket = table[index]
        bucket.findBy { it.key == key }?.let {
            it.value = value
            return
        }
        bucket.addFirst(item.copy()) // добавляем копию, чтобы избежать проблем с мутабельностью значения
    }

    fun remove(ticket: ReaderTicket){
        val index = hash(ticket)
        val bucket = table[index]
        bucket.deleteFirstBy { it.key == ticket }
    }

    /**
     * Поиск читателей по фрагменту ФИО с использованием алгоритма Бойера-Мура
     * 
     * @param fullNameFragment фрагмент ФИО для поиска
     * @return список найденных читателей
     */
    fun findByFullName(fullNameFragment: String): Array<Reader> {
        if (fullNameFragment.isBlank()) return toArray()
        
        var result = arrayOf<Reader>()
        table.forEach { bucket ->
            bucket.forEach { item ->
                if (BoyerMoore.contains(item.value.fullName, fullNameFragment)) {
                    result += item.value
                }
            }
        }
        return result
    }

    fun findByTicket(readerTicket: ReaderTicket): Reader? {
        val index = hash(readerTicket)
        val bucket = table[index]
        return bucket.findBy { it.key == readerTicket }?.value
    }

    operator fun contains(readerTicket: ReaderTicket): Boolean = findByTicket(readerTicket) != null


    fun toArray(): Array<Reader> {
        var result = arrayOf<Reader>()
        table.forEach { bucket ->
            bucket.forEach { item ->
                result += item.value

            }
        }
        return result
    }

    fun clear() {
        table.forEach { it.clear() }
    }

}