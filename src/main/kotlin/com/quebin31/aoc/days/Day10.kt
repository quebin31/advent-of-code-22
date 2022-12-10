package com.quebin31.aoc.days

import com.quebin31.aoc.core.Day
import java.lang.StringBuilder

@Suppress("unused")
class Day10 : Day {

    override fun part1(input: Sequence<String>): Any =
        input.sumSignalStrengths()

    override fun part2(input: Sequence<String>): Any =
        input.getCRTDisplayOutput()

    private fun Sequence<String>.simulateMachine(onCycle: (register: Int, cycle: Int) -> Unit) {
        var register = 1
        var cycle = 1

        forEach { line ->
            val split = line.split(" ")

            when (split.first()) {
                "noop" -> {
                    onCycle(register, cycle)
                    cycle += 1
                }

                "addx" -> {
                    repeat(times = 2) {
                        onCycle(register, cycle + it)
                    }

                    cycle += 2
                    register += split[1].toInt()
                }
            }
        }
    }

    private fun Sequence<String>.sumSignalStrengths(): Int {
        var signalStrengthSum = 0

        simulateMachine { register, cycle ->
            if ((cycle - 20) % 40 == 0) {
                signalStrengthSum += register * cycle
            }
        }

        return signalStrengthSum
    }

    private fun Sequence<String>.getCRTDisplayOutput(): String {
        val builder = StringBuilder().appendLine()

        fun getPixel(spriteMidX: Int, index: Int): Char =
            if (spriteMidX - 1 <= index && index <= spriteMidX + 1) {
                '#'
            } else {
                '.'
            }

        simulateMachine { register, cycle ->
            val pixel = getPixel(spriteMidX = register, index = (cycle - 1) % 40)
            builder.append(pixel)
            if (cycle % 40 == 0) {
                builder.appendLine()
            }
        }

        return builder.toString()
    }
}
