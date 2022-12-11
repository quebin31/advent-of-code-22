package com.quebin31.aoc.days

import com.quebin31.aoc.core.Day


@Suppress("unused")
class Day04 : Day {

    override fun part1(input: Sequence<String>): Any = parseInput(input).count { (a, b) ->
        a.contains(b) || b.contains(a)
    }

    override fun part2(input: Sequence<String>): Any = parseInput(input).count { (a, b) ->
        a.overlaps(b)
    }

    private fun parseInput(input: Sequence<String>) = input
        .map { line ->
            val (a, b) = line.split(",")
            val (aMin, aMax) = a.split("-").map { it.toInt() }
            val (bMin, bMax) = b.split("-").map { it.toInt() }
            (aMin..aMax) to (bMin..bMax)
        }

    private fun IntRange.contains(other: IntRange): Boolean =
        other.first >= first && other.last <= last

    private fun IntRange.overlaps(other: IntRange): Boolean =
        other.last >= first && other.first <= last
}
