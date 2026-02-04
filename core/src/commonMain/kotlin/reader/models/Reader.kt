package reader.models

/**
 * Данные о читателе
 *
 * @param fullName полное имя (ФИО)
 * @param yearOfBirthday год рождения
 * @param address адрес
 * @param placeOfWork место работы и учёбы
 */
data class Reader(val fullName: String, val yearOfBirthday: Int, val address: String, val placeOfWork: String){
    init{
        require(yearOfBirthday in 1870..2130) // можно заменить на проверку с текущим годом, но это излишне усложнит этот data class
    }
}
