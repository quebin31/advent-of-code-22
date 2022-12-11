package com.quebin31.aoc.days

import com.quebin31.aoc.core.Day
import kotlin.math.sign

@Suppress("unused")
class Day09 : Day {

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

                    val x = tail.x + (prev.x - tail.x).sign
                    val y = tail.y + (prev.y - tail.y).sign
                    if (x != prev.x || y != prev.y) {
                        Point(x, y).also { new ->
                            tails[index] = new
                            if (index == tails.lastIndex) visited.add(new)
                        }
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
