package ui.screen.settings

import androidx.lifecycle.ViewModel
import book.models.Book
import book.models.BookCipher
import data.books.BooksRepository
import data.books.BooksRepositorySingleton
import data.loans.LoansRepository
import data.loans.LoansRepositorySingleton
import data.readers.ReadersRepository
import data.readers.ReadersRepositorySingleton
import reader.models.Reader
import reader.models.ReaderTicket


class SettingsViewModel(
    private val readersRepository: ReadersRepository = ReadersRepositorySingleton.readersRepository,
    private val booksRepository: BooksRepository = BooksRepositorySingleton.booksRepository,
    private val loansRepository: LoansRepository = LoansRepositorySingleton.loansRepository
) : ViewModel() {

    fun fillReadersData() {
        testReaders.forEach {
            readersRepository.upsertReader(it)
        }
    }

    fun fillBooksData() {
        testBooks.forEach {
            booksRepository.upsertBook(it)
        }
    }

    fun fillAllData() {
        fillReadersData()
        fillBooksData()
    }

    fun clearReadersData() {
        readersRepository.clear()
    }

    fun clearBooksData() {
        booksRepository.clear()
    }

    fun clearLoansData() {
        loansRepository.clear()
    }

    fun clearAllData() {
        clearLoansData()
        clearReadersData()
        clearBooksData()
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

val testBooks = listOf(
    // Раздел 001 - Художественная литература (классика)
    Book(BookCipher("001.001"), "Война и мир", "Л.Н. Толстой", "Эксмо", 2020, 5, 3),
    Book(BookCipher("001.002"), "Преступление и наказание", "Ф.М. Достоевский", "АСТ", 2019, 4, 2),
    Book(BookCipher("001.003"), "Мастер и Маргарита", "М.А. Булгаков", "Азбука", 2021, 6, 4),
    Book(BookCipher("001.004"), "Евгений Онегин", "А.С. Пушкин", "Детская литература", 2018, 3, 3),
    Book(BookCipher("001.005"), "Анна Каренина", "Л.Н. Толстой", "Эксмо", 2020, 4, 1),
    Book(BookCipher("001.006"), "Идиот", "Ф.М. Достоевский", "АСТ", 2019, 3, 2),
    Book(BookCipher("001.007"), "Отцы и дети", "И.С. Тургенев", "Просвещение", 2017, 5, 5),
    Book(BookCipher("001.008"), "Мертвые души", "Н.В. Гоголь", "Азбука", 2020, 4, 0),
    
    // Раздел 002 - Научная фантастика
    Book(BookCipher("002.001"), "1984", "Дж. Оруэлл", "АСТ", 2021, 5, 2),
    Book(BookCipher("002.002"), "Солярис", "С. Лем", "АСТ", 2019, 3, 3),
    Book(BookCipher("002.003"), "Пикник на обочине", "А. и Б. Стругацкие", "АСТ", 2020, 4, 1),
    Book(BookCipher("002.004"), "Дюна", "Ф. Герберт", "Эксмо", 2021, 6, 4),
    Book(BookCipher("002.005"), "Конец Вечности", "А. Азимов", "Эксмо", 2018, 2, 2),
    
    // Раздел 003 - Программирование
    Book(BookCipher("003.001"), "Чистый код", "Р. Мартин", "Питер", 2022, 8, 3),
    Book(BookCipher("003.002"), "Kotlin в действии", "Д. Жемеров, С. Исакова", "ДМК Пресс", 2021, 5, 2),
    Book(BookCipher("003.003"), "Алгоритмы. Построение и анализ", "Т. Кормен", "Вильямс", 2020, 4, 1),
    Book(BookCipher("003.004"), "Паттерны проектирования", "Э. Гамма и др.", "Питер", 2019, 3, 0),
    Book(BookCipher("003.005"), "Структуры данных и алгоритмы в Java", "Р. Лафоре", "Питер", 2018, 4, 3),
    
    // Раздел 004 - История
    Book(BookCipher("004.001"), "История России", "В.О. Ключевский", "Академический проект", 2015, 3, 2),
    Book(BookCipher("004.002"), "Вторая мировая война", "У. Черчилль", "Альпина", 2020, 2, 1),
    Book(BookCipher("004.003"), "Древний Рим", "М. Бирд", "Альпина", 2019, 3, 3),
    
    // Раздел 005 - Философия
    Book(BookCipher("005.001"), "Так говорил Заратустра", "Ф. Ницше", "АСТ", 2020, 2, 1),
    Book(BookCipher("005.002"), "Государство", "Платон", "Азбука", 2018, 3, 2),
    Book(BookCipher("005.003"), "Критика чистого разума", "И. Кант", "Наука", 2017, 2, 2)
)

