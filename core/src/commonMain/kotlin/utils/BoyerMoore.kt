package utils

/**
 * Алгоритм Бойера-Мура для поиска подстроки в строке
 * 
 * Возвращает индекс первого вхождения pattern в text, или -1 если не найдено
 */
object BoyerMoore {
    
    /**
     * Построение таблицы сдвигов по плохому символу (bad character rule)
     */
    private fun buildBadCharTable(pattern: String): Map<Char, Int> {
        val table = mutableMapOf<Char, Int>()
        val m = pattern.length
        
        for (i in 0 until m - 1) {
            table[pattern[i].lowercaseChar()] = m - 1 - i
        }
        
        return table
    }
    
    /**
     * Поиск подстроки pattern в строке text
     * Регистронезависимый поиск
     * 
     * @return индекс первого вхождения или -1 если не найдено
     */
    fun search(text: String, pattern: String): Int {
        if (pattern.isEmpty()) return 0
        if (text.isEmpty() || pattern.length > text.length) return -1
        
        val textLower = text.lowercase()
        val patternLower = pattern.lowercase()
        
        val n = textLower.length
        val m = patternLower.length
        
        val badCharTable = buildBadCharTable(patternLower)
        
        var i = m - 1 // индекс в тексте
        
        while (i < n) {
            var j = m - 1 // индекс в паттерне
            var k = i // текущий индекс в тексте для сравнения
            
            // Сравниваем символы справа налево
            while (j >= 0 && textLower[k] == patternLower[j]) {
                j--
                k--
            }
            
            if (j < 0) {
                // Паттерн найден
                return k + 1
            }
            
            // Вычисляем сдвиг по таблице плохого символа
            val shift = badCharTable[textLower[k]] ?: m
            i += maxOf(1, shift - (m - 1 - j))
        }
        
        return -1
    }
    
    /**
     * Проверяет, содержит ли text подстроку pattern
     */
    fun contains(text: String, pattern: String): Boolean {
        return search(text, pattern) != -1
    }
    
    /**
     * Находит все вхождения pattern в text
     * 
     * @return список индексов всех вхождений
     */
    fun searchAll(text: String, pattern: String): List<Int> {
        if (pattern.isEmpty()) return emptyList()
        if (text.isEmpty() || pattern.length > text.length) return emptyList()
        
        val results = mutableListOf<Int>()
        var startIndex = 0
        
        while (startIndex <= text.length - pattern.length) {
            val index = search(text.substring(startIndex), pattern)
            if (index == -1) break
            
            results.add(startIndex + index)
            startIndex += index + 1
        }
        
        return results
    }
}
