package com.quebin31.aoc.days

import com.quebin31.aoc.core.Day

@Suppress("unused")
class Day9 : Day {

    data class Point(val x: Int, val y: Int)

    override fun part1(input: Sequence<String>): Any =
        input.simulate(numOfTails = 1)

    override fun part2(input: Sequence<String>): Any =
        input.simulate(numOfTails = 9)

    private fun Sequence<String>.simulate(numOfTails: Int): Int {
        var head = Point(x = 0, y = 0)
        val tails = MutableList(size = numOfTails) { head.copy() }
        val visited = mutableSetOf(tails.last())

        fun move(ticks: Int, update: (Point) -> Point) {
            repeat(ticks) {
                head = update(head)

                tails.scanIndexed(initial = head) { index, prev: Point?, tail ->
                    if (prev == null) return@scanIndexed null

                    val x = tail.x + (prev.x - tail.x).coerceIn(-1..1)
                    val y = tail.y + (prev.y - tail.y).coerceIn(-1..1)
                    val newTail = Point(x, y)
                    if (newTail != prev) {
                        tails[index] = newTail
                        newTail.also { if (index == tails.lastIndex) visited.add(it) }
                    } else {
                        null
                    }
                }
            }
        }

        forEach { line ->
            val (direction, stepsStr) = line.split(" ")
            val steps = stepsStr.toInt()

            when (direction) {
                "R", "L" -> move(ticks = steps) {
                    it.copy(x = it.x + if (direction == "R") 1 else -1)
                }

                "U", "D" -> move(ticks = steps) {
                    it.copy(y = it.y + if (direction == "U") 1 else -1)
                }

                else -> error("Unreachable state $direction")
            }
        }

        return visited.count()
    }
}