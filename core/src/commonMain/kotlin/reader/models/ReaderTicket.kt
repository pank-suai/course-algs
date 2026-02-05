package reader.models

import kotlin.jvm.JvmInline
import kotlin.math.abs


/**
 * Читательский билет в формате ANNNN-YY,
 * где A - тип доступа (А - абонемент, Ч - читальный зал, В - оба типа),
 * NNNN - номер билета,
 * YY - год выдачи (последние две цифры года)
 */
@JvmInline
value class ReaderTicket(private val value: String) {
    init {
        // ANNNN-YY
        require("[АЧВ]\\d{4}-\\d{2}".toRegex().matches(value))
    }

    val accessType: AccessType
        get() = AccessType.fromChar(value.first())

    val number: Int
        get() = value.substring(1, 5).toInt()

    val year: Int
        get() = 2000 + value.substring(6, 8).toInt()

    fun hash(): Int = abs(value.hashCode())

}


/**
 * Тип доступа читательского билета
 */
enum class AccessType(val char: Char) {
    SUBSCRIPTION('А'), READ_ZONE('Ч'), BOTH('В');

    companion object {
        fun fromChar(char: Char) = when (char) {
            'А' -> SUBSCRIPTION
            'Ч' -> READ_ZONE
            'В' -> BOTH
            else -> error("Unexpected error")
        }
    }
}

fun String.toReaderTicket(): ReaderTicket = ReaderTicket(this)