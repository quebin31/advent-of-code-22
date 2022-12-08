package com.quebin31.aoc.days

import com.quebin31.aoc.core.Day

@Suppress("unused")
class Day7 : Day {

    sealed interface Node

    data class FileNode(
        val prev: DirNode? = null,
        val name: String,
        val size: Int,
    ) : Node {
        override fun toString(): String = "File(name = $name, size = $size)"
    }

    data class DirNode(
        val prev: DirNode? = null,
        val name: String,
        val children: MutableList<Node> = mutableListOf(),
    ) : Node {
        override fun toString(): String = "Dir(name = $name)"

        val dirs: List<DirNode>
            get() = children.filterIsInstance<DirNode>()

        val files: List<FileNode>
            get() = children.filterIsInstance<FileNode>()
    }

    override fun part1(input: Sequence<String>): Any = parseFsTree(input)
        .getDirSizes()
        .values
        .filter { it < Part1MaxSize }
        .sum()

    override fun part2(input: Sequence<String>): Any {
        val sizes = parseFsTree(input).getDirSizes()
        val free = Part2AvailSize - sizes["/"]!!
        val needed = Part2ReqSize - free
        return sizes.values.sorted().find { it >= needed } ?: 0
    }

    private fun DirNode.getDirSizes(): Map<String, Int> {
        var currSum = 0
        val cwdStack = ArrayDeque<String>()
        val dirSizes = mutableMapOf<String, Int>()

        walkTree(
            onEnter = { node ->
                when (node) {
                    is FileNode -> {
                        currSum += node.size
                    }

                    is DirNode -> {
                        cwdStack.add(node.name)
                    }
                }
            },
            onLeave = { node ->
                if (node is DirNode) {
                    val path = cwdStack.joinToString("/")
                    cwdStack.removeLast()

                    val childrenDirSizeSum = dirSizes
                        .filterKeys { name -> name in node.dirs.map { "$path/${it.name}" } }
                        .values
                        .sum()

                    dirSizes[path] = currSum + childrenDirSizeSum
                    currSum = 0
                }
            },
        )

        return dirSizes
    }

    private fun parseFsTree(input: Sequence<String>): DirNode {
        val rootDir = DirNode(name = "/")
        var currDir: DirNode? = null
        val files = mutableListOf<FileNode>()

        for (line in input.filter { it.isNotBlank() }) {
            when {
                line.startsWith(CdPrefix) -> {
                    val dir = line.substring(CdPrefix.length).trim()
                    currDir?.children?.addAll(files)
                    files.clear()

                    currDir = if (dir == "..") {
                        currDir?.prev
                    } else {
                        if (currDir != null) {
                            DirNode(prev = currDir, name = dir).also(currDir.children::add)
                        } else {
                            rootDir
                        }
                    }
                }

                line.startsWith(LsPrefix) -> {
                    /* no-op */
                }

                else -> {
                    val (prefix, name) = line.split(" ")
                    if (prefix == "dir") continue

                    files.add(FileNode(prev = currDir, name = name, size = prefix.toInt()))
                }
            }
        }

        if (files.isNotEmpty()) {
            currDir!!.children.addAll(files)
        }

        return rootDir
    }

    private fun DirNode.walkTree(onEnter: (Node) -> Unit, onLeave: (Node) -> Unit) {
        onEnter(this)

        for (dir in dirs) {
            dir.walkTree(onEnter, onLeave)
        }

        for (file in files) {
            onEnter(file)
        }

        onLeave(this)
    }

    companion object {
        private const val CdPrefix = "$ cd"
        private const val LsPrefix = "$ ls"

        private const val Part1MaxSize = 100_000
        private const val Part2AvailSize = 70_000_000
        private const val Part2ReqSize = 30_000_000
    }
}
