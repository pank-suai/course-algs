package reader.models

import utils.currentYear

/**
 * Данные о читателе
 *
 * @param readerTicket номер читательского билета (указываю для представления)
 * @param fullName полное имя (ФИО)
 * @param yearOfBirthday год рождения
 * @param address адрес
 * @param placeOfWork место работы и учёбы
 */
data class Reader(val readerTicket: ReaderTicket, val fullName: String, val yearOfBirthday: Int, val address: String, val placeOfWork: String){
    init{
        require(validateYearOfBirthday(yearOfBirthday))
    }

    companion object{
        fun validateYearOfBirthday(yearOfBirthday: Int) = yearOfBirthday in 1870..currentYear()
    }
}
