package com.quebin31.aoc.days

import com.quebin31.aoc.core.Day

@Suppress("unused")
class Day3 : Day {

    override fun part1(input: Sequence<String>): Any = input
        .sumOf { line ->
            val (a, b) = line
                .chunked(size = line.length / 2)
                .map(String::toSet)

            a.intersect(b).first().priority()
        }

    override fun part2(input: Sequence<String>): Any = input
        .map(String::toSet)
        .chunked(size = 3) { it.reduce { acc, set -> acc.intersect(set) } }
        .sumOf { it.first().priority() }

    private fun Char.priority(): Int = if (this in 'a'..'z') {
        this - 'a' + 1
    } else {
        this - 'A' + 27
    }
}
