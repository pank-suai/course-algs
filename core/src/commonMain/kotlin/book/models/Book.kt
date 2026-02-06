package book.models

/**
 * Данные о книге
 *
 * @param cipher шифр книги в формате "NNN.MMM", где NNN - номер раздела, MMM - номер книги в разделе
 * @param name название книги
 * @param authors авторы книги
 * @param publishing издательство
 * @param yearOfPublishing год издания
 * @param totalCopies общее количество экземпляров книги в библиотеке
 * @param availableCopies количество доступных экземпляров книги в библиотеке
 */
data class Book(
    val cipher: BookCipher,
    val name: String,
    val authors: String,
    val publishing: String,
    val yearOfPublishing: Int,
    val totalCopies: Int,
    val availableCopies: Int,
): Comparable<Book>{

    init {
        require(yearOfPublishing in 1450..2130) // существование книг до 1450 года крайне сомнительно, а 2130 - это уже слишком далеко в будущем
        require(totalCopies >= 0)
        require(availableCopies in 0..totalCopies)
    }

    // Сравнение по шифру
    override fun compareTo(other: Book): Int = cipher.compareTo(other.cipher)
}
