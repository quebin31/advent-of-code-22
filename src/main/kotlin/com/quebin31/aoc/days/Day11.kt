package com.quebin31.aoc.days

import com.quebin31.aoc.core.Day
import com.quebin31.aoc.utils.chunkedBy

@Suppress("unused")
class Day11 : Day {

    data class Monkey(
        val items: MutableList<Long>,
        val operation: (Long) -> Long,
        val base: Int,
        val test: (Long) -> Int,
    )

    override fun part1(input: Sequence<String>): Any =
        parseInput(input).monkeyBusinessAfter(rounds = 20, isLenient = true)

    override fun part2(input: Sequence<String>): Any =
        parseInput(input).monkeyBusinessAfter(rounds = 10_000, isLenient = false)

    private fun List<Monkey>.monkeyBusinessAfter(rounds: Int, isLenient: Boolean): Long {
        val inspections = MutableList(size = size) { 0L }
        val commonBase = map { it.base }.reduce(Int::times).toLong()

        repeat(times = rounds) {
            forEachIndexed { index, monkey ->
                monkey.items.toList().forEach { worryLevel ->
                    inspections[index] += 1L

                    val newWorryLevel = monkey.operation(worryLevel).let { level ->
                        if (isLenient) {
                            level.floorDiv(3)
                        } else {
                            level % commonBase
                        }
                    }

                    val rxMonkey = get(monkey.test(newWorryLevel))
                    rxMonkey.items.add(newWorryLevel)
                }

                monkey.items.clear()
            }
        }

        return inspections
            .sortedDescending()
            .take(n = 2)
            .reduce { acc, i -> acc * i }
    }

    private fun parseInput(input: Sequence<String>): List<Monkey> = input
        .chunkedBy { it.isBlank() }
        .map { monkey ->
            val items = parseItems(monkey[1])
            val operation = parseOperation(monkey[2])
            val base = parseBase(monkey[3])
            val test = parseTest(monkey.drop(n = 3))

            Monkey(items, operation, base, test)
        }
        .toList()

    private fun parseItems(input: String): MutableList<Long> = input
        .substringAfter(delimiter = ':')
        .trim()
        .split(", ")
        .map { it.toLong() }
        .toMutableList()

    private fun parseOperation(input: String): (Long) -> Long {
        val operation = input
            .substringAfter(delimiter = '=')
            .trim()
            .split(" ")

        val op: Long.(Long) -> Long = when (operation[1]) {
            "*" -> Long::times
            "+" -> Long::plus
            else -> error("Invalid operator")
        }

        return { value ->
            val a = operation[0].toLongOrNull() ?: value
            val b = operation[2].toLongOrNull() ?: value
            a.op(b)
        }
    }

    private fun parseBase(input: String): Int =
        input.dropWhile { !it.isDigit() }.toInt()

    private fun parseTest(input: List<String>): (Long) -> Int {
        val divisibleBy = input[0].dropWhile { !it.isDigit() }.toInt()
        val ifTrue = input[1].dropWhile { !it.isDigit() }.toInt()
        val ifFalse = input[2].dropWhile { !it.isDigit() }.toInt()
        return {
            if (it % divisibleBy == 0L) {
                ifTrue
            } else {
                ifFalse
            }
        }
    }
}
