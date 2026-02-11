package ui.screen.readers.upsert

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import data.loans.LoansRepository
import data.loans.LoansRepositorySingleton
import data.readers.ReadersRepository
import data.readers.ReadersRepositorySingleton
import reader.models.Reader
import reader.models.ReaderTicket

class UpsertReaderViewModel(
    selectedReaderTicket: String? = null, // Если null, то это добавление нового читателя, иначе - редактирование существующего
    private val readersRepository: ReadersRepository = ReadersRepositorySingleton.readersRepository,
    private val loansRepository: LoansRepository = LoansRepositorySingleton.loansRepository
) :
    ViewModel() {

    var errorMessage: String? by mutableStateOf(null)

    var readerTicket: String by mutableStateOf("")
    var fullName: String by mutableStateOf("")
    var yearOfBirthday: String by mutableStateOf("")
    var address: String by mutableStateOf("")
    var placeOfWork: String by mutableStateOf("")

    // Информация о выданных книгах
    val activeLoansCount: Int
        get() = if (ReaderTicket.validateTicket(readerTicket)) {
            loansRepository.getActiveByReaderTicket(ReaderTicket(readerTicket)).size
        } else 0

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

    fun removeReader(): Boolean {
        if (!ReaderTicket.validateTicket(readerTicket)) {
            errorMessage = "Невозможно удалить читателя с некорректным номером билета"
            return false
        }
        
        // Проверка наличия невозвращенных книг
        val ticket = ReaderTicket(readerTicket)
        if (loansRepository.hasActiveLoansForReader(ticket)) {
            val activeLoans = loansRepository.getActiveByReaderTicket(ticket)
            errorMessage = "Невозможно снять читателя с обслуживания: у него ${activeLoans.size} невозвращенных книг"
            return false
        }
        
        readersRepository.removeReader(ticket)
        return true
    }

}