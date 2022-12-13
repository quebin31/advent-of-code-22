package com.quebin31.aoc.days

import com.quebin31.aoc.core.Day
import com.quebin31.aoc.utils.chunkedBy
import kotlin.math.min

typealias Packet = List<Any>

@Suppress("unused")
class Day13 : Day {

    override fun part1(input: Sequence<String>): Any = input
        .chunkedBy { it.isBlank() }
        .map { (a, b) -> a.toPacket() to b.toPacket() }
        .mapIndexed { index, (a, b) -> if (a.isInRightOrderWith(b) != false) index + 1 else 0 }
        .sum()

    @Suppress("UNCHECKED_CAST")
    override fun part2(input: Sequence<String>): Any = input
        .filter(String::isNotBlank)
        .map { it.toPacket() }
        .plus(listOf(DividerPacketA, DividerPacketB))
        .sortedWith(packetComparator)
        .mapIndexed { index, packet ->
            if (packet == DividerPacketA || packet == DividerPacketB) {
                index + 1
            } else {
                1
            }
        }
        .reduce(Int::times)

    private val packetComparator = Comparator<Packet> { a, b ->
        when (a.isInRightOrderWith(b)) {
            true -> -1
            null -> 0
            else -> 1
        }
    }

    private fun Packet.isInRightOrderWith(other: Packet): Boolean? {
        val minLastIndex = min(lastIndex, other.lastIndex)

        for (index in 0..minLastIndex) {
            val a = get(index)
            val b = other[index]

            when {
                a is Int && b is Int -> {
                    if (a < b) return true
                    if (a > b) return false
                }

                a is List<*> && b is List<*> -> {
                    @Suppress("UNCHECKED_CAST")
                    val result = (a as Packet).isInRightOrderWith(b as Packet)
                    if (result != null) return result
                }

                else -> {
                    @Suppress("UNCHECKED_CAST")
                    val result = if (a is List<*>) {
                        (a as Packet).isInRightOrderWith(listOf(b))
                    } else {
                        listOf(a).isInRightOrderWith(b as Packet)
                    }

                    if (result != null) return result
                }
            }
        }

        return when {
            size < other.size -> true
            size > other.size -> false
            else -> null
        }
    }

    private fun CharSequence.toPacket(): Packet {
        if (length == 2) return emptyList()

        val cleansed = subSequence(1, lastIndex)
        val packet = mutableListOf<Any>()
        var sepStart = 0
        var nestedLevel = 0
        var nestedStart = 0

        cleansed.forEachIndexed { index, c ->
            when (c) {
                '[' -> {
                    if (nestedLevel == 0) nestedStart = index
                    nestedLevel += 1
                }

                ']' -> {
                    nestedLevel -= 1
                    if (nestedLevel == 0) {
                        packet.add(cleansed.subSequence(nestedStart, index + 1).toPacket())
                    }
                }

                ',' -> {
                    if (cleansed[index - 1] != ']' && nestedLevel == 0) {
                        packet.add(cleansed.substring(sepStart, index).toInt())
                    }

                    sepStart = index + 1
                }
            }
        }

        if (cleansed.last() != ']') {
            packet.add(cleansed.substring(sepStart).toInt())
        }

        return packet
    }

    companion object {
        private val DividerPacketA: Packet = listOf(listOf(2))
        private val DividerPacketB: Packet = listOf(listOf(6))
    }
}
