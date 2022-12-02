package com.quebin31.aoc.days

import com.quebin31.aoc.core.Day

@Suppress("unused")
class Day2 : Day {

    enum class Result {
        Lose, Draw, Win,
        ;

        val points: Int = ordinal * 3

        companion object {
            private val Values = values()

            fun decode(string: String, coding: String): Result =
                Values[coding.indexOf(string)]
        }
    }

    enum class Hand {
        Rock, Paper, Scissors,
        ;

        val points: Int = ordinal + 1

        fun play(other: Hand): Result = when (other.ordinal) {
            ordinal -> Result.Draw
            (ordinal + 1).mod(3) -> Result.Lose
            else -> Result.Win
        }

        fun neededHandFor(result: Result): Hand = when (result) {
            Result.Draw -> this
            Result.Lose -> Values[(ordinal - 1).mod(3)]
            Result.Win -> Values[(ordinal + 1).mod(3)]
        }

        companion object {
            private val Values = values()

            fun decode(string: String, coding: String): Hand =
                Values[coding.indexOf(string)]
        }
    }

    override fun part1(input: Sequence<String>): Any = parsePart1(input)
        .fold(initial = 0) { acc, (elfHand, myHand) ->
            acc + myHand.points + myHand.play(elfHand).points
        }

    override fun part2(input: Sequence<String>): Any = parsePart2(input)
        .fold(initial = 0) { acc, (elfHand, result) ->
            val myHand = elfHand.neededHandFor(result)
            acc + myHand.points + result.points
        }

    private fun parsePart1(input: Sequence<String>) = input
        .map { line ->
            val pair = line.split(" ")
            val elfHand = Hand.decode(pair[0], coding = "ABC")
            val myHand = Hand.decode(pair[1], coding = "XYZ")
            elfHand to myHand
        }

    private fun parsePart2(input: Sequence<String>) = input
        .map { line ->
            val pair = line.split(" ")
            val elfHand = Hand.decode(pair[0], coding = "ABC")
            val result = Result.decode(pair[1], coding = "XYZ")
            elfHand to result
        }
}
