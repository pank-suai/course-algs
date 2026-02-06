package book

import book.models.Book
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

// TODO: добавить обход (симметричный) для поиска по фрагментам ФИО автора или названия книги
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


}