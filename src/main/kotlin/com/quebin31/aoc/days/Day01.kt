package com.quebin31.aoc.days

import com.quebin31.aoc.core.Day
import com.quebin31.aoc.utils.addSorted
import com.quebin31.aoc.utils.chunkedBy
import com.quebin31.aoc.utils.sortedDescending
import java.util.*

@Suppress("unused")
class Day01 : Day {

    override fun part1(input: Sequence<String>): Any =
        maxCaloriesImperative(input, n = 1).single()

    override fun part2(input: Sequence<String>): Any =
        maxCaloriesImperative(input, n = 3).sum()

    /**
     * Elegant. More memory footprint as it uses two hidden stateful [Sequence] operations, one in
     * [chunkedBy] and another one in [sortedDescending].
     */
    private fun maxCaloriesFunctional(input: Sequence<String>, n: Int): List<Int> = input
        .chunkedBy { it.isBlank() }
        .map { inventory -> inventory.sumOf { it.toInt() } }
        .sortedDescending(n)
        .toList()

    /**
     * Has the benefit of using a single for loop with less memory footprint as the sum of
     * calories is kept temporarily in a single variable.
     */
    private fun maxCaloriesImperative(input: Sequence<String>, n: Int): List<Int> {
        val maxCarriedCalories = mutableListOf<Int>()
        var currentSum = 0

        for (line in input) {
            if (line.isBlank()) {
                maxCarriedCalories.update(currentSum, n)
                currentSum = 0
            } else {
                currentSum += line.toInt()
            }
        }

        return maxCarriedCalories.toList()
    }

    /**
     * Updates the [MutableList] adding a new value, and removing the lowest if the size would
     * exceed [n].
     */
    private fun MutableList<Int>.update(value: Int, n: Int) {
        addSorted(reverseOrder(), value)
        if (size > n) {
            removeLast()
        }
    }
}
