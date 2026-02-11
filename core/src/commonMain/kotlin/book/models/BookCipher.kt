package book.models

import kotlin.jvm.JvmInline

/**
 * Шифр книги в формате "NNN.MMM", где NNN - номер раздела, MMM - номер книги в разделе
 */
@JvmInline
value class BookCipher(val value: String): Comparable<BookCipher> {
    
    companion object {
        private val CIPHER_REGEX = "\\d{3}\\.\\d{3}".toRegex()
        
        /**
         * Проверка валидности шифра
         */
        fun validate(value: String): Boolean = CIPHER_REGEX.matches(value)
    }
    
    init {
        require(validate(value))
    }

    /**
     * Номер раздела
     */
    val section: Int
        get() = value.substring(0, 3).toInt()

    /**
     * Номер книги в разделе
     */
    val number: Int
        get() = value.substring(4, 7).toInt()

    /**
     * Сравнение по разделу, а при равенстве - по номеру
     *
     * Сравнение по строковому представлению как раз подходит
     */
    override fun compareTo(other: BookCipher): Int = value.compareTo(other.value)
}