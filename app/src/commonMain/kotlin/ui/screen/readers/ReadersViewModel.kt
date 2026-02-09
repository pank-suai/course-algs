package ui.screen.readers

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.readers.ReadersRepository
import data.readers.ReadersRepositorySingleton
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import reader.models.Reader


class ReadersViewModel(private val mapReadersRepository: ReadersRepository = ReadersRepositorySingleton.readersRepository) :
    ViewModel() {
    val readers = mapReadersRepository.readersList.stateIn(viewModelScope, SharingStarted.Eagerly, arrayOf())

    fun upsertReader(reader: Reader){
        mapReadersRepository.upsertReader(reader)
    }

    fun removeReader(reader: Reader){
        mapReadersRepository.removeReader(reader)
    }

}