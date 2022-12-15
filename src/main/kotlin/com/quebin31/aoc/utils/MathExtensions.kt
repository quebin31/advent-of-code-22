package com.quebin31.aoc.utils

fun minMax(a: Int, b: Int): Pair<Int, Int> = if (a < b) {
    a to b
} else {
    b to a
}
