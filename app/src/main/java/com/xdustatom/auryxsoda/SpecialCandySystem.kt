package com.xdustatom.auryxsoda

import android.graphics.Point

class SpecialCandySystem {

    fun activateSpecialCandy(board: Array<Array<Tile>>, pos: Point): List<Point> {
        val tile = board[pos.y][pos.x]
        val candy = tile.candy ?: return emptyList()

        return when (candy.special) {
            CandySpecial.LINE_HORIZONTAL -> activateLineHorizontal(board, pos)
            CandySpecial.LINE_VERTICAL -> activateLineVertical(board, pos)
            CandySpecial.BOMB -> activateBomb(board, pos)
            CandySpecial.NONE -> emptyList()
        }
    }

    private fun activateLineHorizontal(board: Array<Array<Tile>>, pos: Point): List<Point> {
        val targets = mutableListOf<Point>()
        val row = pos.y

        for (col in board[0].indices) {
            targets.add(Point(col, row))
        }

        return targets
    }

    private fun activateLineVertical(board: Array<Array<Tile>>, pos: Point): List<Point> {
        val targets = mutableListOf<Point>()
        val col = pos.x

        for (row in board.indices) {
            targets.add(Point(col, row))
        }

        return targets
    }

    private fun activateBomb(board: Array<Array<Tile>>, pos: Point): List<Point> {
        val targets = mutableListOf<Point>()
        val radius = 1

        for (dy in -radius..radius) {
            for (dx in -radius..radius) {
                val newX = pos.x + dx
                val newY = pos.y + dy

                if (isValidPosition(board, newX, newY)) {
                    targets.add(Point(newX, newY))
                }
            }
        }

        return targets
    }

    fun createSpecialCandy(match: Match, board: Array<Array<Tile>>): Point? {
        if (!match.isSpecialMatch()) return null

        val centerTile = match.tiles[match.tiles.size / 2]
        val tile = board[centerTile.y][centerTile.x]

        tile.candy?.let {
            it.special = match.getSpecialType()
            it.scale = 1.2f
            return centerTile
        }

        return null
    }

    private fun isValidPosition(board: Array<Array<Tile>>, x: Int, y: Int): Boolean {
        return y >= 0 && y < board.size && x >= 0 && x < board[0].size
    }
}
