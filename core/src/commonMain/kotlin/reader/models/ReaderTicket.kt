package reader.models

value class ReaderTicket(val value: String) {
    init {
        // ANNNN-YY
        require("[АЧВ]\\d{4}-\\d{2}".toRegex().matches(value))
    }

    val accessType: AccessType
        get() = AccessType.fromChar(value.first())

}

enum class AccessType(val char: Char){
    SUBSCRIPTION('А'), READ_ZONE('Ч'), BOTH('В');

    companion object{
        fun fromChar(char: Char) = when (char){
            'A' -> SUBSCRIPTION
            'Ч' -> READ_ZONE
            'В' -> BOTH
            else -> error("Unexpected error")
        }
    }
}