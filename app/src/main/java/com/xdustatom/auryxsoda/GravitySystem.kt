package com.xdustatom.auryxsoda

import android.graphics.Point
import android.graphics.PointF

class GravitySystem {

    fun applyGravity(board: Array<Array<Tile>>, tileSize: Float): Boolean {
        var hasMovement = false
        val rows = board.size
        val cols = board[0].size

        // Process from bottom to top
        for (col in 0 until cols) {
            var writeRow = rows - 1

            // Move existing candies down
            for (row in rows - 1 downTo 0) {
                val tile = board[row][col]
                if (!tile.isEmpty()) {
                    if (row != writeRow) {
                        // Move candy down
                        board[writeRow][col].candy = tile.candy
                        board[writeRow][col].candy?.startMoveTo(
                            PointF(col * tileSize, writeRow * tileSize)
                        )
                        tile.candy = null
                        hasMovement = true
                    }
                    writeRow--
                }
            }

            // Fill empty spaces from top
            for (row in writeRow downTo 0) {
                if (board[row][col].isEmpty()) {
                    val newCandy = createRandomCandy()
                    newCandy.position = PointF(col * tileSize, -tileSize * (writeRow - row + 1))
                    newCandy.startMoveTo(PointF(col * tileSize, row * tileSize))
                    board[row][col].candy = newCandy
                    hasMovement = true
                }
            }
        }

        return hasMovement
    }

    fun updateAnimations(board: Array<Array<Tile>>, speed: Float): Boolean {
        var hasAnimations = false

        for (row in board) {
            for (tile in row) {
                tile.candy?.let {
                    if (it.isAnimating) {
                        it.updateAnimation(speed)
                        hasAnimations = true
                    }
                }
            }
        }

        return hasAnimations
    }

    private fun createRandomCandy(): Candy {
        val types = arrayOf(
            CandyType.RED,
            CandyType.BLUE,
            CandyType.GREEN,
            CandyType.YELLOW,
            CandyType.PURPLE,
            CandyType.ORANGE
        )
        return Candy(types.random())
    }
}
