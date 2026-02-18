package loan

import book.models.BookCipher
import loan.models.Loan
import reader.models.ReaderTicket

class LoanSkipList() {
    class Node(
        val loan: Loan,
        var next: Node? = null,
        var nextBookGroup: Node? = null, // указатель на следующую группу по шифру книги
        var nextReaderRecord: Node? = null // указатель на следующую запись по читательскому билету
    )

    var head: Node? = null


    fun insert(loan: Loan){
        val newNode = Node(loan)
        if (head == null) {
            head = newNode
            return
        }

        // вставляем в отсортированную позицию по шифру книги (Сортировка включением)
        if (head == null || loan.bookCipher < head!!.loan.bookCipher) {
            newNode.next = head
            head = newNode
        } else {
            var curr = head
            while (curr?.next != null && curr.next!!.loan.bookCipher <= loan.bookCipher) {
                curr = curr.next
            }
            newNode.next = curr?.next
            curr?.next = newNode
        }

        updateBookGroup()

        updateReaderRecord()
    }

    // Обновление указателей на группы по шифру книги
    private fun updateBookGroup(){
        var curr = head
        while (curr != null) {
            var nextGroup = curr.next
            while (nextGroup != null && nextGroup.loan.bookCipher == curr.loan.bookCipher) {
                nextGroup = nextGroup.next
            }
            curr.nextBookGroup = nextGroup
            curr = nextGroup
        }
    }

    // Обновление указателей на группы по читательскому билету
    private fun updateReaderRecord(){
        var curr = head
        while (curr != null) {
            var nextRecord = curr.next
            while (nextRecord != null && nextRecord.loan.readerTicket == curr.loan.readerTicket) {
                nextRecord = nextRecord.next
            }
            curr.nextReaderRecord = nextRecord
            curr = nextRecord
        }
    }

    fun findByBookCipher(bookCipher: BookCipher): Array<Loan>{
        var result = arrayOf<Loan>()

        var curr = head
        while (curr != null) {
            if (curr.loan.bookCipher == bookCipher) {
                result += curr.loan
                curr = curr.next
                continue
            } else if (curr.loan.bookCipher > bookCipher) {
                break
            }
            curr = curr.nextBookGroup
        }
        return result
    }

    fun findByReaderTicket(readerTicket: ReaderTicket): Array<Loan>{
        var result = arrayOf<Loan>()

        var curr = head
        while (curr != null) {
            if (curr.loan.readerTicket == readerTicket) {
                result += curr.loan
                curr = curr.next
                continue
            }
            curr = curr.nextReaderRecord
        }
        return result
    }

    /**
     * Найти активную выдачу (без даты возврата) по шифру книги и номеру читательского билета
     */
    fun findActiveLoan(bookCipher: BookCipher, readerTicket: ReaderTicket): Loan? {
        var curr = head
        while (curr != null) {
            if (curr.loan.bookCipher == bookCipher && 
                curr.loan.readerTicket == readerTicket && 
                curr.loan.returnDate == null) {
                return curr.loan
            }
            curr = curr.next
        }
        return null
    }

    /**
     * Найти все активные выдачи по шифру книги (без даты возврата)
     */
    fun findActiveByBookCipher(bookCipher: BookCipher): Array<Loan> {
        return findByBookCipher(bookCipher).filter { it.returnDate == null }.toTypedArray()
    }

    /**
     * Найти все активные выдачи по номеру читательского билета (без даты возврата)
     */
    fun findActiveByReaderTicket(readerTicket: ReaderTicket): Array<Loan> {
        return findByReaderTicket(readerTicket).filter { it.returnDate == null }.toTypedArray()
    }

    /**
     * Удалить запись о выдаче
     */
    fun remove(loan: Loan): Boolean {
        if (head == null) return false

        // Если удаляем голову
        if (head!!.loan == loan) {
            head = head!!.next
            updateBookGroup()
            updateReaderRecord()
            return true
        }

        var curr = head
        while (curr?.next != null) {
            if (curr.next!!.loan == loan) {
                curr.next = curr.next!!.next
                updateBookGroup()
                updateReaderRecord()
                return true
            }
            curr = curr.next
        }
        return false
    }

    /**
     * Заменить запись о выдаче (для обновления даты возврата)
     */
    fun update(oldLoan: Loan, newLoan: Loan): Boolean {
        if (remove(oldLoan)) {
            insert(newLoan)
            return true
        }
        return false
    }

    /**
     * Преобразовать список в массив
     */
    fun toArray(): Array<Loan> {
        var result = arrayOf<Loan>()
        var curr = head
        while (curr != null) {
            result += curr.loan
            curr = curr.next
        }
        return result
    }

    /**
     * Очистить список
     */
    fun clear() {
        head = null
    }

    /**
     * Проверить наличие активных выдач для читателя
     */
    fun hasActiveLoansForReader(readerTicket: ReaderTicket): Boolean {
        return findActiveByReaderTicket(readerTicket).isNotEmpty()
    }

    /**
     * Проверить наличие активных выдач для книги
     */
    fun hasActiveLoansForBook(bookCipher: BookCipher): Boolean {
        return findActiveByBookCipher(bookCipher).isNotEmpty()
    }
}