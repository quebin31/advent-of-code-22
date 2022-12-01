package com.quebin31.aoc.utils

/**
 * Adds this value in a way such that the list stays sorted according to the [comparator], this
 * **assumes** that the receiver [MutableList] is already sorted with the same [comparator] logic.
 */
fun <T : Comparable<T>> MutableList<T>.addSorted(comparator: Comparator<T>, element: T) {
    for (idx in 0..lastIndex) {
        val value = get(idx)
        if (comparator.compare(value, element) >= 0) {
            add(idx, element)
            return
        }
    }

    add(element)
}
