package book

import book.models.Book
import utils.BoyerMoore
import kotlin.math.max

/**
 * Узел дерева для хранения книг
 *
 * @param book данные о книге
 * @param left левый потомок
 * @param right правый потомок
 */
class Node(
    var book: Book,
    var left: Node? = null,
    var right: Node? = null
) {
    /**
     * Высота узла
     */
    val height: Int get() = max(left?.height ?: 0, right?.height ?: 0) + 1

    /**
     * Баланс узла
     */
    val balance: Int get() = (right?.height ?: 0) - (left?.height ?: 0)

    /**
     * Наличие потомков
     */
    val hasChild: Boolean get() = right != null || left != null

    /**
     * Является ли узел листом
     */
    val isLeaf: Boolean get() = right == null && left == null

}

/**
 * Дерево для хранения книг
 */
class BookAVLTree {
    /**
     * Корневой узел дерева
     */
    var root: Node? = null

    /**
     * Высота дерева
     */
    val height
        get() = root?.height ?: 0


    /**
     * Вставка книги в дерево
     */
    fun insert(book: Book) {
        root = insert(root, book)
    }


    private fun insert(root: Node?, book: Book): Node {
        // Если корня нет
        if (root == null) {
            return Node(book)
        }

        // Если число больше, то вправо
        if (root.book < book) {
            root.right = insert(root.right, book)
        } else if (root.book > book) { //  меньше влево
            root.left = insert(root.left, book)
        } else {
            return root // Только уникальные значения
        }
        return balance(root)
    }

    // Поднимаем левый элемент
    private fun rotateRight(node: Node): Node? {
        val newNode = node.left
        node.left = newNode?.right
        newNode?.right = node
        return newNode
    }

    private fun rotateLeft(node: Node): Node? {
        val newNode = node.right
        node.right = newNode?.left
        newNode?.left = node
        return newNode
    }

    // Балансировка дерева
    private fun balance(node: Node): Node {
        if (node.balance == 2) { // Тогда правое больше
            if ((node.right?.balance ?: 0) < 0) { // Необходим большой поворот
                node.right = rotateRight(node.right!!)
            }
            return rotateLeft(node)!!
        } else if (node.balance == -2) {
            if ((node.left?.balance ?: 0) > 0) {
                node.left = rotateLeft(node.left!!)
            }
            return rotateRight(node)!!
        }
        return node // Нет необходимости балансировки
    }

    fun delete(book: Book) {
        root = delete(root, book)
    }


    /**
     * Рекурсивная функция удаления
     */
    private fun delete(node: Node?, book: Book): Node? {
        if (node == null) {
            return null
        }
        val updatedNode: Node?
        if (book > node.book) {
            node.right = delete(node.right, book)
            updatedNode = node
        } else if (book < node.book) {
            node.left = delete(node.left, book)
            updatedNode = node
        } else {
            if (node.left == null || node.right == null) {
                updatedNode = node.left ?: node.right
            } else {
                val minNode = minNode(node.right!!)
                node.book = minNode.book
                node.right = delete(node.right, minNode.book)
                updatedNode = node
            }
        }
        return balance(updatedNode ?: return null)
    }


    fun minNode(node: Node): Node {
        var current = node
        while (current.left != null) {
            current = current.left!!
        }
        return current
    }

    /**
     * Поиск книги по шифру
     */
    fun findByCipher(cipher: book.models.BookCipher): Book? {
        var current = root
        while (current != null) {
            val cmp = cipher.compareTo(current.book.cipher)
            current = when {
                cmp < 0 -> current.left
                cmp > 0 -> current.right
                else -> return current.book
            }
        }
        return null
    }

    /**
     * Обновление книги (удаление старой и вставка новой)
     */
    fun update(book: Book) {
        val existing = findByCipher(book.cipher)
        if (existing != null) {
            delete(existing)
        }
        insert(book)
    }

    /**
     * Симметричный обход дерева (in-order traversal)
     * Возвращает книги в отсортированном порядке по шифру
     */
    fun inOrderTraversal(action: (Book) -> Unit) {
        inOrderTraversal(root, action)
    }

    private fun inOrderTraversal(node: Node?, action: (Book) -> Unit) {
        if (node == null) return
        inOrderTraversal(node.left, action)
        action(node.book)
        inOrderTraversal(node.right, action)
    }

    /**
     * Преобразование дерева в массив (симметричный обход)
     */
    fun toArray(): Array<Book> {
        val list = mutableListOf<Book>()
        inOrderTraversal { list.add(it) }
        return list.toTypedArray()
    }

    /**
     * Очистка дерева
     */
    fun clear() {
        root = null
    }

    /**
     * Проверка наличия книги по шифру
     */
    fun contains(cipher: book.models.BookCipher): Boolean = findByCipher(cipher) != null

    /**
     * Поиск книг по фрагменту ФИО автора или названия
     * Использует симметричный обход дерева и алгоритм Бойера-Мура
     * 
     * @param fragment фрагмент для поиска (может быть частью имени автора или названия)
     * @return массив найденных книг
     */
    fun searchByFragment(fragment: String): Array<Book> {
        if (fragment.isBlank()) return toArray()
        
        val results = mutableListOf<Book>()
        inOrderTraversal { book ->
            // Ищем фрагмент в авторах или названии с помощью алгоритма Бойера-Мура
            if (BoyerMoore.contains(book.authors, fragment) || 
                BoyerMoore.contains(book.name, fragment)) {
                results.add(book)
            }
        }
        return results.toTypedArray()
    }

    /**
     * Поиск книг только по фрагменту ФИО автора
     * Использует симметричный обход дерева и алгоритм Бойера-Мура
     */
    fun searchByAuthorFragment(fragment: String): Array<Book> {
        if (fragment.isBlank()) return toArray()
        
        val results = mutableListOf<Book>()
        inOrderTraversal { book ->
            if (BoyerMoore.contains(book.authors, fragment)) {
                results.add(book)
            }
        }
        return results.toTypedArray()
    }

    /**
     * Поиск книг только по фрагменту названия
     * Использует симметричный обход дерева и алгоритм Бойера-Мура
     */
    fun searchByNameFragment(fragment: String): Array<Book> {
        if (fragment.isBlank()) return toArray()
        
        val results = mutableListOf<Book>()
        inOrderTraversal { book ->
            if (BoyerMoore.contains(book.name, fragment)) {
                results.add(book)
            }
        }
        return results.toTypedArray()
    }

}