package com.quebin31.aoc.days

import com.quebin31.aoc.core.Day

typealias ForestMap = List<List<Int>>

@Suppress("unused")
class Day08 : Day {

    override fun part1(input: Sequence<String>): Any = parseForestMap(input)
        .countVisibleTrees()

    override fun part2(input: Sequence<String>): Any = parseForestMap(input)
        .findHighestScenicScore()

    private fun ForestMap.countVisibleTrees(): Int {
        var visibleTrees = 0
        val highestHor = MutableList(size = size) { -1 }
        val highestVer = MutableList(size = first().size) { -1 }

        for (i in 0..lastIndex) {
            val column = get(i)
            for (j in 0..column.lastIndex) {
                var isHigher = false
                val tree = column[j]
                if (tree > highestHor[i]) {
                    isHigher = true
                    highestHor[i] = tree
                }

                if (tree > highestVer[j]) {
                    isHigher = true
                    highestVer[j] = tree
                }

                when {
                    isHigher || i == 0 || i == lastIndex || j == 0 || j == lastIndex -> {
                        visibleTrees += 1
                    }

                    else -> {
                        val rightMax = column.drop(n = j + 1).max()
                        if (tree > rightMax) {
                            visibleTrees += 1
                        } else {
                            val bottomMax = drop(n = i + 1).maxOf { it[j] }
                            if (tree > bottomMax) {
                                visibleTrees += 1
                            }
                        }
                    }
                }
            }
        }

        return visibleTrees
    }

    private fun ForestMap.findHighestScenicScore(): Int {
        var highestScore = 0

        for (i in 1 until lastIndex) {
            val column = get(i)
            for (j in 1 until column.lastIndex) {
                val tree = column[j]

                val rightScore = column.asSequence()
                    .drop(n = j + 1)
                    .indexOfFirst { it >= tree }
                    .ifNegative { column.size - j - 2 }
                    .inc()

                val leftScore = column.asReversed().asSequence()
                    .drop(n = column.size - j)
                    .indexOfFirst { it >= tree }
                    .ifNegative { j - 1 }
                    .inc()

                val bottomScore = asSequence()
                    .drop(n = i + 1)
                    .indexOfFirst { it[j] >= tree }
                    .ifNegative { size - i - 2 }
                    .inc()

                val topScore = asReversed().asSequence()
                    .drop(n = size - i)
                    .indexOfFirst { it[j] >= tree }
                    .ifNegative { i - 1 }
                    .inc()

                val score = rightScore * leftScore * bottomScore * topScore
                if (score > highestScore) {
                    highestScore = score
                }
            }
        }

        return highestScore
    }

    private fun Int.ifNegative(value: () -> Int): Int = if (this < 0) value() else this

    private fun parseForestMap(input: Sequence<String>): ForestMap = input
        .map { line -> line.map { it - '0' } }
        .toList()
}
