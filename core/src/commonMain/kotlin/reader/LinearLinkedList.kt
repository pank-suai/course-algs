package reader

/**
 * Упрощённый односвязный список для реализации открытого хеширования
 */
class LinearLinkedList<T> {
    class Item<T>(var value: T, var next: Item<T>? = null)

    private var _first: Item<T>? = null
    private var _size = 0

    val size get() = _size

    val first: Item<T>?
        get() = _first



    /**
     * Добавление в начало
     */
    fun addFirst(value: T) {
        _first = Item(value, _first)
        _size++
    }

    /**
     * Удаление по переданному условию
     */
    fun deleteFirstBy(predicate: (T) -> Boolean) {
        if (_first == null) return
        var current = _first
        while (current?.next != null && current.next?.value?.let { !predicate(it) } == true) {
            current = current.next
        }

        if (current == _first) {
            val next = _first!!.next
            _first!!.next = null // Боже храни GC
            _first = next
            _size--
            return
        }

        val next = current?.next ?: return
        current.next = next.next
        next.next = null
        _size--
    }

    /**
     * Поиск по переданному условию
     * @return найденный элемент или null
     */
    fun findBy(predicate: (T) -> Boolean): T? {

        var current = _first

        while (current != null && current.value?.let { !predicate(it) } == true) {
            current = current.next
        }
        return current?.value
    }



    /**
     * Итерация по всем элементам списка
     */
    inline fun forEach(function: (T) -> Unit) {
        if (first == null) return
        var current = first

        while (current != null){
            function(current.value)
            current = current.next
        }
    }

    fun clear(){
        // Слава Garbage Collector
        _first = null
        _size = 0
    }
}