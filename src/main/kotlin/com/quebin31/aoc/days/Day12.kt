package com.quebin31.aoc.days

import com.quebin31.aoc.core.Day

@Suppress("unused")
class Day12 : Day {

    data class Point(val x: Int, val y: Int)

    data class HeightMap(
        val start: Point,
        val end: Point,
        val map: List<List<Char>>,
    )

    override fun part1(input: Sequence<String>): Any =
        parseInput(input).fewestStepsToHighestLevel() ?: -1

    override fun part2(input: Sequence<String>): Any =
        parseInput(input).fewestStepsToLowestLevel() ?: -1

    private fun HeightMap.fewestStepsToHighestLevel(): Int? =
        findShortestPathFromEndTo { it == start }

    private fun HeightMap.fewestStepsToLowestLevel(): Int? =
        findShortestPathFromEndTo { map[it.y][it.x] == 'a' }

    private fun HeightMap.findShortestPathFromEndTo(predicate: (Point) -> Boolean): Int? {
        val visited = mutableSetOf(end)
        val queue = ArrayDeque<Pair<Point, Int>>().apply { add(end to 0) }

        while (queue.isNotEmpty()) {
            val (point, distance) = queue.removeFirst()
            if (predicate(point)) {
                return distance
            } else {
                for (neighbor in validNeighbors(point, visited)) {
                    visited.add(neighbor)
                    queue.add(neighbor to distance + 1)
                }
            }
        }

        return null
    }

    private fun HeightMap.pointAt(x: Int, y: Int): Point? =
        map.getOrNull(y)?.getOrNull(x)?.let { Point(x, y) }

    private fun HeightMap.neighbors(point: Point): List<Point> = listOfNotNull(
        pointAt(x = point.x - 1, y = point.y),
        pointAt(x = point.x + 1, y = point.y),
        pointAt(x = point.x, y = point.y - 1),
        pointAt(x = point.x, y = point.y + 1),
    )

    private fun HeightMap.validNeighbors(point: Point, visited: Set<Point>): List<Point> =
        neighbors(point)
            .filterNot { it in visited }
            .filter { map[it.y][it.x] + 1 >= map[point.y][point.x] }

    private fun parseInput(input: Sequence<String>): HeightMap {
        var start: Point? = null
        var end: Point? = null

        val map = input
            .mapIndexed { y, line ->
                line.mapIndexed { x, c ->
                    when (c) {
                        'S' -> 'a'.also { start = Point(x, y) }
                        'E' -> 'z'.also { end = Point(x, y) }
                        else -> c
                    }
                }
            }
            .toList()

        return HeightMap(start!!, end!!, map)
    }
}
