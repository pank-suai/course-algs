package data.loans

import book.models.BookCipher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import loan.LoanSkipList
import loan.models.Loan
import reader.models.ReaderTicket

/**
 * Репозиторий выдачи книг, который использует слоеный список (Skip List)
 */
class SkipListLoansRepository : LoansRepository {
    private val loansSkipList = LoanSkipList()

    private val loanList = MutableStateFlow<Array<Loan>>(arrayOf())

    override val loansList: Flow<Array<Loan>> = loanList

    override fun getLoansByBookCipher(cipher: BookCipher): Array<Loan> = 
        loansSkipList.findByBookCipher(cipher)

    override fun getLoansByReaderTicket(readerTicket: ReaderTicket): Array<Loan> = 
        loansSkipList.findByReaderTicket(readerTicket)

    override fun getActiveByBookCipher(cipher: BookCipher): Array<Loan> = 
        loansSkipList.findActiveByBookCipher(cipher)

    override fun getActiveByReaderTicket(readerTicket: ReaderTicket): Array<Loan> = 
        loansSkipList.findActiveByReaderTicket(readerTicket)

    override fun hasActiveLoansForReader(readerTicket: ReaderTicket): Boolean = 
        loansSkipList.hasActiveLoansForReader(readerTicket)

    override fun hasActiveLoansForBook(cipher: BookCipher): Boolean = 
        loansSkipList.hasActiveLoansForBook(cipher)

    override fun issueLoan(loan: Loan) {
        loansSkipList.insert(loan)
        updateLoanList()
    }

    override fun returnLoan(loan: Loan, returnedLoan: Loan): Boolean {
        val result = loansSkipList.update(loan, returnedLoan)
        if (result) updateLoanList()
        return result
    }

    override fun removeLoan(loan: Loan): Boolean {
        val result = loansSkipList.remove(loan)
        if (result) updateLoanList()
        return result
    }

    override fun clear() {
        loansSkipList.clear()
        loanList.value = loansSkipList.toArray()
    }

    private fun updateLoanList() {
        loanList.value = loansSkipList.toArray()
    }
}
