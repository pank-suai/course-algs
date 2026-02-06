import book.BookAVLTree
import book.models.Book
import book.models.BookCipher
import kotlin.test.Test
import kotlin.test.assertEquals

class BookAVLTreeTest {
    val books = listOf(
        Book(
            cipher = BookCipher("100.001"),
            name = "Война и мир",
            authors = "Лев Толстой",
            publishing = "АСТ",
            yearOfPublishing = 2010,
            totalCopies = 5,
            availableCopies = 3
        ),
        Book(
            cipher = BookCipher("100.002"),
            name = "Преступление и наказание",
            authors = "Федор Достоевский",
            publishing = "Мир",
            yearOfPublishing = 2005,
            totalCopies = 4,
            availableCopies = 2
        ),
        Book(
            cipher = BookCipher("100.000"),
            name = "Мастер и Маргарита",
            authors = "Михаил Булгаков",
            publishing = "ЭКСМО",
            yearOfPublishing = 2015,
            totalCopies = 3,
            availableCopies = 1
        ),
        Book(
            cipher = BookCipher("200.001"),
            name = "Алгоритмы на Java",
            authors = "Роберт Седжвик, Кевин Уэйн",
            publishing = "Вильямс",
            yearOfPublishing = 2013,
            totalCopies = 2,
            availableCopies = 2
        ),
        Book(
            cipher = BookCipher("200.002"),
            name = "Совершенный код",
            authors = "Стив Макконнелл",
            publishing = "Русская редакция",
            yearOfPublishing = 2018,
            totalCopies = 3,
            availableCopies = 0
        ),
        Book(
            cipher = BookCipher("300.001"),
            name = "История России",
            authors = "Вячеслав Ключевский",
            publishing = "Академический проект",
            yearOfPublishing = 2012,
            totalCopies = 6,
            availableCopies = 4
        ),
        Book(
            cipher = BookCipher("150.005"),
            name = "Граф Монте-Кристо",
            authors = "Александр Дюма",
            publishing = "Литература",
            yearOfPublishing = 2007,
            totalCopies = 2,
            availableCopies = 1
        ),
        Book(
            cipher = BookCipher("250.003"),
            name = "Системное проектирование",
            authors = "Жан-Луи Гассе",
            publishing = "Питер",
            yearOfPublishing = 2014,
            totalCopies = 1,
            availableCopies = 1
        )
    )




    // Тест на вставку книги в дерево и проверку структуры дерева после вставки
    @Test
    fun rootAVLTree() {
        val tree = BookAVLTree()
        tree.insert(books[0])
        assertEquals(books[0], tree.root?.book)

        val rightBook = books[1]
        tree.insert(rightBook)
        assertEquals(rightBook, tree.root?.right?.book)
        assertEquals(books[0], tree.root?.book)
        assertEquals(null, tree.root?.left)

        val leftBook = books[2]
        tree.insert(leftBook)
        assertEquals(rightBook, tree.root?.right?.book)
        assertEquals(leftBook, tree.root?.left?.book)
        assertEquals(books[0], tree.root?.book)
    }

    @Test
    fun balanceAVLTree() {
        val tree = BookAVLTree()
        for (book in books) {
            tree.insert(book)
        }

        /*
         * Структура AVL-дерева после вставки всех книг:
         *
         *                    200.001
         *                   /       \
         *              100.001        250.003
         *              /     \        /      \
         *         100.000  100.002  200.002  300.001
         *                      \
         *                    150.005
         *
         * Высота дерева: 4
         * Корень: 200.001 (Алгоритмы на Java)
         */

        // Проверяем корень
        assertEquals(books[3], tree.root?.book) // 200.001 - Алгоритмы на Java

        // Проверяем высоту дерева
        assertEquals(4, tree.height)

        // Проверяем левое поддерево (корень 100.001)
        assertEquals(books[0], tree.root?.left?.book) // 100.001 - Война и мир
        assertEquals(books[2], tree.root?.left?.left?.book) // 100.000 - Мастер и Маргарита
        assertEquals(books[1], tree.root?.left?.right?.book) // 100.002 - Преступление и наказание
        assertEquals(books[6], tree.root?.left?.right?.right?.book) // 150.005 - Граф Монте-Кристо

        // Проверяем правое поддерево (корень 250.003)
        assertEquals(books[7], tree.root?.right?.book) // 250.003 - Системное проектирование
        assertEquals(books[4], tree.root?.right?.left?.book) // 200.002 - Совершенный код
        assertEquals(books[5], tree.root?.right?.right?.book) // 300.001 - История России

        // Проверяем балансировку узлов (баланс должен быть в пределах [-1, 1])
        assertEquals(-1, tree.root?.balance) // Корень: левая ветка на 1 выше
        assertEquals(1, tree.root?.left?.balance) // Левое поддерево: правая ветка на 1 выше
        assertEquals(0, tree.root?.right?.balance) // Правое поддерево сбалансировано
    }
}