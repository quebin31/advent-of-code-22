package com.quebin31.aoc.days

import com.quebin31.aoc.core.Day
import com.quebin31.aoc.utils.chunkedBy

@Suppress("unused")
class Day05 : Day {

    data class Instruction(val amount: Int, val orig: Int, val dest: Int)

    data class Program(val stacks: List<ArrayDeque<Char>>, val instructions: List<Instruction>)

    override fun part1(input: Sequence<String>): Any = parseInput(input).run {
        execute(isCrateMover9001 = false)
        getCratesAtTheTop()
    }

    override fun part2(input: Sequence<String>): Any = parseInput(input).run {
        execute(isCrateMover9001 = true)
        getCratesAtTheTop()
    }

    private fun Program.getCratesAtTheTop(): String =
        stacks.map { it.first() }.joinToString(separator = "")

    private fun Program.execute(isCrateMover9001: Boolean) {
        for (instruction in instructions) {
            val crates = stacks[instruction.orig]
                .take(instruction.amount)
                .let { if (isCrateMover9001) it.asReversed() else it }

            crates.forEach { crate ->
                stacks[instruction.dest].addFirst(crate)
            }

            repeat(crates.size) {
                stacks[instruction.orig].removeFirst()
            }
        }
    }

    private fun parseInput(input: Sequence<String>): Program {
        val (crates, instructions) = input.chunkedBy { it.isBlank() }.toList()

        val invertedStacks = crates
            .dropLast(n = 1)
            .map { line -> line.windowed(size = 3, step = 4).map { it[1] } }

        val numOfStacks = invertedStacks.maxOf { it.size }
        val stacks = MutableList(size = numOfStacks) { ArrayDeque<Char>() }

        invertedStacks.forEach { line ->
            line.forEachIndexed { index, crate ->
                if (!crate.isWhitespace()) {
                    stacks[index].add(crate)
                }
            }
        }

        val finalInstructions = instructions.map { line ->
            val (amount, orig, dest) = line
                .split(" ")
                .filterIndexed { index, _ -> index % 2 == 1 }
                .mapIndexed { index, value ->
                    val shift = index.coerceAtMost(1)
                    value.toInt() - shift
                }

            Instruction(amount, orig, dest)
        }

        return Program(stacks = stacks, instructions = finalInstructions)
    }
}
