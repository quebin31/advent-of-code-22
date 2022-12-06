package com.quebin31.aoc.days

import com.quebin31.aoc.core.Day

@Suppress("unused")
class Day6 : Day {

    override fun part1(input: Sequence<String>): Any = input
        .first()
        .findStartMarkerPosition(length = 4)

    override fun part2(input: Sequence<String>): Any = input
        .first()
        .findStartMarkerPosition(length = 14)

    private fun String.findStartMarkerPosition(length: Int): Int = windowedSequence(size = length)
        .indexOfFirst { it.toSet().size == length }
        .plus(other = length)
}
