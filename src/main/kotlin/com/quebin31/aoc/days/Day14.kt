package com.quebin31.aoc.days

import com.quebin31.aoc.core.Day
import com.quebin31.aoc.utils.minMax
import com.quebin31.aoc.utils.toClosedRange
import kotlin.math.max

// TODO: Rewrite using a grid
@Suppress("unused")
class Day14 : Day {

    override fun part1(input: Sequence<String>): Any = parseInput(input)
        .countUnitsOfSand(withEndlessVoid = true)

    override fun part2(input: Sequence<String>): Any = parseInput(input)
        .fixSignal()
        .countUnitsOfSand(withEndlessVoid = false)

    private fun List<RockPath>.fixSignal(): List<RockPath> {
        val maxDeepness = maxDeepness()
        val y = maxDeepness + 2
        val segment = RockSegment(Point(x = Int.MIN_VALUE, y = y), Point(x = Int.MAX_VALUE, y = y))
        return plus(RockPath(segments = listOf(segment)))
    }

    private fun List<RockPath>.countUnitsOfSand(withEndlessVoid: Boolean): Int {
        val sandHeap = mutableSetOf<Point>()
        var current = Point.SandOrigin

        val stopCondition = if (withEndlessVoid) {
            val maxDeepness = maxDeepness();
            { current.y >= maxDeepness }
        } else {
            { Point.SandOrigin in sandHeap }
        }

        while (!stopCondition()) {
            current.nextPoint(allRocks = this, heap = sandHeap)?.let { next ->
                current = next
            } ?: run {
                sandHeap.add(current)
                current = Point.SandOrigin
            }
        }

        return sandHeap.count()
    }

    private fun Point.nextPoint(allRocks: List<RockPath>, heap: Set<Point>): Point? {
        val possibleMoves = listOf(
            copy(y = y + 1),
            copy(y = y + 1, x = x - 1),
            copy(y = y + 1, x = x + 1),
        )

        return possibleMoves.firstOrNull { it !in heap && !allRocks.collidesWith(it) }
    }

    private fun List<RockPath>.collidesWith(point: Point): Boolean =
        any { it.collidesWith(point) }

    private fun List<RockPath>.maxDeepness(): Int =
        maxOf { rocks -> rocks.segments.maxOf { it.maxY } }

    data class Point(val x: Int, val y: Int) {

        companion object {
            val SandOrigin = Point(x = 500, y = 0)
        }
    }

    data class RockSegment(val beg: Point, val end: Point) {

        val maxY: Int = max(beg.y, end.y)
        private val minMaxHorRange = minMax(beg.x, end.x).toClosedRange()
        private val minMaxVerRange = minMax(beg.y, end.y).toClosedRange()

        fun collidesWith(point: Point): Boolean = if (beg.x != end.x) {
            point.y == beg.y && point.x in minMaxHorRange
        } else {
            point.x == beg.x && point.y in minMaxVerRange
        }
    }

    data class RockPath(val segments: List<RockSegment>) {

        fun collidesWith(point: Point): Boolean = segments
            .any { it.collidesWith(point) }
    }

    private fun parseInput(input: Sequence<String>): List<RockPath> = input
        .map { line ->
            val segments = line
                .split("->")
                .map { segment ->
                    val (x, y) = segment
                        .trim()
                        .split(",")
                        .map(String::toInt)

                    Point(x, y)
                }
                .windowed(size = 2) { (beg, end) -> RockSegment(beg, end) }

            RockPath(segments)
        }
        .toList()
}
