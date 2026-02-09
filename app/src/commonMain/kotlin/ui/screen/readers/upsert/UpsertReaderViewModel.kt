package ui.screen.readers.upsert

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import data.readers.ReadersRepository
import data.readers.ReadersRepositorySingleton
import reader.models.Reader
import reader.models.ReaderTicket

class UpsertReaderViewModel(
    selectedReaderTicket: String? = null, // Если null, то это добавление нового читателя, иначе - редактирование существующего
    private val readersRepository: ReadersRepository = ReadersRepositorySingleton.readersRepository
) :
    ViewModel() {

    var errorMessage: String? by mutableStateOf(null)

    var readerTicket: String by mutableStateOf("")
    var fullName: String by mutableStateOf("")
    var yearOfBirthday: String by mutableStateOf("")
    var address: String by mutableStateOf("")
    var placeOfWork: String by mutableStateOf("")


    init {
        if (selectedReaderTicket != null) {
            val reader = readersRepository.getReaderByTicket(ReaderTicket(selectedReaderTicket))
            if (reader != null) {
                readerTicket = reader.readerTicket.value
                fullName = reader.fullName
                yearOfBirthday = reader.yearOfBirthday.toString()
                address = reader.address
                placeOfWork = reader.placeOfWork
            }

        }
    }

    fun addReader(): Boolean {

        if (!ReaderTicket.validateTicket(readerTicket) ) {
            errorMessage =
                "Читательский билет должен быть в формате ANNNN-YY, где где A - тип доступа (А - абонемент, Ч - читальный зал, В - оба типа), NNNN - номер билета, YY - год выдачи"
            return false
        }
        if (readersRepository.getReaderByTicket(ReaderTicket(readerTicket)) != null)
        {
            errorMessage = "Читательский билет уже существует"
            return false
        }
        if (fullName.isBlank() || yearOfBirthday.isBlank() || address.isBlank() || placeOfWork.isBlank()) {
            errorMessage = "Поля не должны быть пустыми"
            return false
        }
        if (!"\\d{4}".toRegex()
                .matches(yearOfBirthday) || !Reader.validateYearOfBirthday(
                yearOfBirthday.toInt()
            )
        ) {
            errorMessage = "Год рождения должен быть числом от 1870 до текущего года"
            return false
        }

        val reader = Reader(
            readerTicket = ReaderTicket(readerTicket),
            fullName = fullName,
            yearOfBirthday = yearOfBirthday.toInt(),
            address = address,
            placeOfWork = placeOfWork
        )
        readersRepository.upsertReader(reader)
        errorMessage = null

        return true
    }

}