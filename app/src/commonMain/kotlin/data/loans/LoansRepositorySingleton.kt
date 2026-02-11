package data.loans

/**
 * Синглтон для репозитория выдачи книг
 */
object LoansRepositorySingleton {
    val loansRepository: LoansRepository = SkipListLoansRepository()
}
