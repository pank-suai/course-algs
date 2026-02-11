package ui.screen.settings

import androidx.lifecycle.ViewModel
import data.readers.ReadersRepository
import data.readers.ReadersRepositorySingleton
import reader.models.Reader
import reader.models.ReaderTicket


class SettingsViewModel(private val mapReadersRepository: ReadersRepository = ReadersRepositorySingleton.readersRepository) :
    ViewModel() {

    fun fillData() {
        testReaders.forEach {
            mapReadersRepository.upsertReader(it)
        }
    }


    fun clearAllData() {
        mapReadersRepository.clear()
    }
}


val testReaders = listOf(
    Reader(ReaderTicket("А0001-23"), "Иванов Иван Иванович", 1990, "ул. Ленина, 1", "ООО Ромашка"),
    Reader(ReaderTicket("Ч0002-23"), "Петров Петр Петрович", 1985, "ул. Мира, 5", "ПАО Газпром"),
    Reader(ReaderTicket("В0003-24"), "Сидоров Сидор Сидорович", 2000, "пр. Славы, 10", "ИТМО"),
    Reader(ReaderTicket("А0004-22"), "Кузнецов Алексей Сергеевич", 1995, "ул. Садовая, 12", "СПбГУ"),
    Reader(ReaderTicket("Ч0005-23"), "Смирнова Ольга Викторовна", 1988, "ул. Пушкина, 3", "Школа №123"),
    Reader(ReaderTicket("В0006-24"), "Попов Дмитрий Андреевич", 2002, "пр. Просвещения, 45", "ГУАП"),
    Reader(ReaderTicket("А0007-21"), "Васильев Игорь Олегович", 1975, "ул. Кирова, 7", "Завод Арсенал"),
    Reader(ReaderTicket("Ч0008-23"), "Соколов Максим Игоревич", 1992, "пр. Культуры, 15", "Магазин 24"),
    Reader(ReaderTicket("В0009-22"), "Михайлова Елена Юрьевна", 1983, "ул. Веденеева, 8", "Больница им. Боткина"),
    Reader(ReaderTicket("А0010-24"), "Новиков Артем Владимирович", 2001, "пр. Просвещения, 10", "ГУАП"),
    Reader(ReaderTicket("Ч0011-23"), "Федоров Степан Павлович", 1998, "ул. Морская, 22", "Порт СПб"),
    Reader(ReaderTicket("В0012-24"), "Морозова Анна Дмитриевна", 2005, "ул. Летчика Пилютова, 5", "Университет МВД"),
    Reader(ReaderTicket("А0013-22"), "Волков Кирилл Александрович", 1994, "ул. Казанская, 4", "Ресторан Гинза"),
    Reader(ReaderTicket("Ч0014-23"), "Лебедева Кристина Егоровна", 1980, "ул. Академика Байкова, 3", "НИИ Точности"),
    Reader(ReaderTicket("В0015-24"), "Семенов Денис Максимович", 1999, "пр. Энгельса, 111", "ТЦ Гранд Каньон")
)

