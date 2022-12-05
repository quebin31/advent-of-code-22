package com.quebin31.aoc.utils

/**
 * Chunk the given sequence using the [separator] function, returns a [Sequence] that contains
 * each chunk.
 *
 * _This operation is intermediate and stateful_
 */
fun <T> Sequence<T>.chunkedBy(separator: (T) -> Boolean): Sequence<List<T>> = sequence {
    val currentList = mutableListOf<T>()

    for (item in this@chunkedBy) {
        if (separator(item)) {
            yield(currentList.toList())
            currentList.clear()
        } else {
            currentList.add(item)
        }
    }

    if (currentList.isNotEmpty()) {
        yield(currentList)
    }
}

/**
 * Returns a sequence that yields elements of this sequence sorted descending according to their
 * natural sorting order. This sequence only yields [n] values.
 *
 * _This operation is intermediate and stateful_
 */
fun <T: Comparable<T>> Sequence<T>.sortedDescending(n: Int): Sequence<T> {
    val sortedList = mutableListOf<T>()
    val comparator = reverseOrder<T>()

    for (item in this) {
        sortedList.addSorted(comparator, item)
        if (sortedList.size > n) {
            sortedList.removeLast()
        }
    }

    return sortedList.asSequence()
}
