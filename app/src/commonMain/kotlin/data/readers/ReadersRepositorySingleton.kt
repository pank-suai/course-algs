package data.readers

/**
 * Синглтон для репозитория читателей
 */
object ReadersRepositorySingleton {
    val readersRepository: ReadersRepository = MapReadersRepository()
}