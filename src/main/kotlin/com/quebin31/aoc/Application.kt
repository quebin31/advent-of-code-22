package com.quebin31.aoc

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.int
import com.quebin31.aoc.core.Day
import java.io.File

object Application : CliktCommand() {

    private val dayNumber: Int by option(help = "Day number for implementation discovery")
        .int()
        .required()

    private val test: Boolean by option(help = "Read test input instead of the real one")
        .flag()

    private val stackTrace: Boolean by option(help = "Show error stacktrace")
        .flag()

    override fun run() {
        val title = "Advent of Code 2022 - Day $dayNumber"
        println(title)
        println(title.replace(".".toRegex(), "-"))

        val day = Day.fromNumber(dayNumber)
        if (day == null) {
            println("Couldn't find an implementation for this day")
            return
        }

        val padNumber = "$dayNumber".padStart(length = 2, padChar = '0')
        val inputName = if (test) "test.txt" else "real.txt"
        val inputPath = "./input/$padNumber/$inputName"

        File(inputPath).run {
            day.safeRun(part = 1) { useLines(block = ::part1) }
            day.safeRun(part = 2) { useLines(block = ::part2) }
        }
    }

    private fun Day.safeRun(part: Int, block: Day.() -> Any) = try {
        val output = block()
        println("Part $part = $output")
    } catch (e: Throwable) {
        val message = if (stackTrace) {
            e.stackTraceToString()
        } else {
            e.message ?: "Something went wrong"
        }

        println("Part $part = Error! $message")
    }
}

fun main(args: Array<String>) = Application.main(args)

