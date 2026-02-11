package data.books

/**
 * Синглтон для репозитория книг
 */
object BooksRepositorySingleton {
    val booksRepository: BooksRepository = AVLBooksRepository()
}
