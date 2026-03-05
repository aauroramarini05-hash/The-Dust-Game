package com.xdustatom.auryxsoda

import android.graphics.Point

class MatchDetector {

    fun findAllMatches(board: Array<Array<Tile>>): List<Match> {
        val matches = mutableListOf<Match>()
        val rows = board.size
        val cols = board[0].size

        // Find horizontal matches
        for (row in 0 until rows) {
            var currentType: CandyType? = null
            var matchTiles = mutableListOf<Point>()

            for (col in 0 until cols) {
                val tile = board[row][col]
                if (!tile.isEmpty() && tile.candy?.type != CandyType.EMPTY) {
                    val candyType = tile.candy?.type

                    if (candyType == currentType) {
                        matchTiles.add(Point(col, row))
                    } else {
                        if (matchTiles.size >= 3) {
                            matches.add(Match(matchTiles.toList(), currentType!!, true))
                        }
                        matchTiles = mutableListOf(Point(col, row))
                        currentType = candyType
                    }
                } else {
                    if (matchTiles.size >= 3) {
                        matches.add(Match(matchTiles.toList(), currentType!!, true))
                    }
                    matchTiles.clear()
                    currentType = null
                }
            }

            if (matchTiles.size >= 3) {
                matches.add(Match(matchTiles.toList(), currentType!!, true))
            }
        }

        // Find vertical matches
        for (col in 0 until cols) {
            var currentType: CandyType? = null
            var matchTiles = mutableListOf<Point>()

            for (row in 0 until rows) {
                val tile = board[row][col]
                if (!tile.isEmpty() && tile.candy?.type != CandyType.EMPTY) {
                    val candyType = tile.candy?.type

                    if (candyType == currentType) {
                        matchTiles.add(Point(col, row))
                    } else {
                        if (matchTiles.size >= 3) {
                            matches.add(Match(matchTiles.toList(), currentType!!, false))
                        }
                        matchTiles = mutableListOf(Point(col, row))
                        currentType = candyType
                    }
                } else {
                    if (matchTiles.size >= 3) {
                        matches.add(Match(matchTiles.toList(), currentType!!, false))
                    }
                    matchTiles.clear()
                    currentType = null
                }
            }

            if (matchTiles.size >= 3) {
                matches.add(Match(matchTiles.toList(), currentType!!, false))
            }
        }

        return mergeOverlappingMatches(matches)
    }

    private fun mergeOverlappingMatches(matches: List<Match>): List<Match> {
        if (matches.isEmpty()) return matches

        val merged = mutableListOf<Match>()
        val processed = mutableSetOf<Int>()

        for (i in matches.indices) {
            if (i in processed) continue

            var currentMatch = matches[i]
            val overlapping = mutableListOf<Match>()

            for (j in i + 1 until matches.size) {
                if (j in processed) continue

                if (hasOverlap(currentMatch, matches[j])) {
                    overlapping.add(matches[j])
                    processed.add(j)
                }
            }

            if (overlapping.isNotEmpty()) {
                val allTiles = (currentMatch.tiles + overlapping.flatMap { it.tiles }).toSet().toList()
                currentMatch = Match(allTiles, currentMatch.type, currentMatch.isHorizontal)
            }

            merged.add(currentMatch)
            processed.add(i)
        }

        return merged
    }

    private fun hasOverlap(match1: Match, match2: Match): Boolean {
        if (match1.type != match2.type) return false
        return match1.tiles.any { it in match2.tiles }
    }

    fun canSwap(board: Array<Array<Tile>>, pos1: Point, pos2: Point): Boolean {
        if (!isAdjacent(pos1, pos2)) return false
        if (!isValidPosition(board, pos1) || !isValidPosition(board, pos2)) return false

        val tile1 = board[pos1.y][pos1.x]
        val tile2 = board[pos2.y][pos2.x]

        if (tile1.isEmpty() || tile2.isEmpty()) return false
        if (tile1.isLocked || tile2.isLocked) return false

        // Simulate swap
        val temp = tile1.candy
        tile1.candy = tile2.candy
        tile2.candy = temp

        val hasMatches = findAllMatches(board).isNotEmpty()

        // Swap back
        val temp2 = tile1.candy
        tile1.candy = tile2.candy
        tile2.candy = temp2

        return hasMatches
    }

    private fun isAdjacent(pos1: Point, pos2: Point): Boolean {
        val dx = Math.abs(pos1.x - pos2.x)
        val dy = Math.abs(pos1.y - pos2.y)
        return (dx == 1 && dy == 0) || (dx == 0 && dy == 1)
    }

    private fun isValidPosition(board: Array<Array<Tile>>, pos: Point): Boolean {
        return pos.y >= 0 && pos.y < board.size && pos.x >= 0 && pos.x < board[0].size
    }
}
