package com.quebin31.aoc.core

interface Day {

    fun part1(input: Sequence<String>): Any

    fun part2(input: Sequence<String>): Any

    companion object {

        fun fromNumber(number: Int): Day? = try {
            val loadedClass = Class.forName("com.quebin31.aoc.days.Day$number")
            loadedClass.getDeclaredConstructor().newInstance() as Day
        } catch (error: Throwable) {
            null
        }
    }
}
