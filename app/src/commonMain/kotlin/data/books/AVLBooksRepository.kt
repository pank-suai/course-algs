package data.books

import book.BookAVLTree
import book.models.Book
import book.models.BookCipher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Репозиторий книг, который использует разработанное AVL-дерево для хранения
 */
class AVLBooksRepository : BooksRepository {
    private var booksTree = BookAVLTree()

    private val bookList = MutableStateFlow<Array<Book>>(arrayOf())

    override val booksList: Flow<Array<Book>> = bookList

    override fun getBookByCipher(cipher: BookCipher): Book? = booksTree.findByCipher(cipher)

    override fun upsertBook(book: Book) {
        booksTree.update(book)
        updateBookList()
    }

    override fun removeBook(book: Book) = removeBook(book.cipher)

    override fun removeBook(cipher: BookCipher) {
        val book = booksTree.findByCipher(cipher)
        if (book != null) {
            booksTree.delete(book)
            updateBookList()
        }
    }

    override fun clear() {
        booksTree.clear()
        bookList.value = booksTree.toArray()
    }

    private fun updateBookList() {
        bookList.value = booksTree.toArray()
    }
}
