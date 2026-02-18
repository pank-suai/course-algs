package ui.screen.readers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.readers.ReadersRepository
import data.readers.ReadersRepositorySingleton
import kotlinx.coroutines.flow.*
import reader.models.Reader
import reader.models.ReaderTicket

enum class SearchBy(val title: String) {
    ReaderTicket("Номер читательского билета"),
    FullName("ФИО")
}

class ReadersViewModel(private val mapReadersRepository: ReadersRepository = ReadersRepositorySingleton.readersRepository) :
    ViewModel() {

    private val _query = MutableStateFlow<String>("")
    var query: StateFlow<String> = _query.asStateFlow()
    var searchBy: SearchBy by mutableStateOf(SearchBy.FullName)


    val readers = mapReadersRepository.readersList.combine(query) { readers, query ->
        if (query.isBlank()) {
            return@combine readers
        }
        return@combine when (searchBy) {
            SearchBy.ReaderTicket if ReaderTicket.validateTicket(query) -> mapReadersRepository.getReaderByTicket(
                ReaderTicket(query)
            )?.let {
                arrayOf(it)
            } ?: arrayOf()


            SearchBy.FullName -> mapReadersRepository.searchByFullName(query)
            else -> readers
        }

    }.stateIn(viewModelScope, SharingStarted.Eagerly, arrayOf())

    fun updateQuery(newQuery: String) {
        _query.value = newQuery
    }

    fun upsertReader(reader: Reader) {
        mapReadersRepository.upsertReader(reader)
    }

    fun removeReader(reader: Reader) {
        mapReadersRepository.removeReader(reader)
    }

}