package com.xdustatom.auryxsoda

import android.graphics.Point
import android.graphics.PointF
import kotlin.random.Random

class BoardManager(private val rows: Int = 8, private val cols: Int = 8) {
    
    private val board: Array<Array<Tile>> = Array(rows) { row ->
        Array(cols) { col ->
            Tile(row, col)
        }
    }
    
    private val matchDetector = MatchDetector()
    private val gravitySystem = GravitySystem()
    private val specialCandySystem = SpecialCandySystem()
    
    fun initialize(tileSize: Float) {
        // Fill board with random candies, ensuring no initial matches
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                var candy: Candy
                do {
                    candy = createRandomCandy()
                    board[row][col].candy = candy
                } while (hasMatchAt(row, col))
                
                candy.position = PointF(col * tileSize, row * tileSize)
                candy.targetPosition = PointF(col * tileSize, row * tileSize)
            }
        }
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
        return Candy(types[Random.nextInt(types.size)])
    }
    
    private fun hasMatchAt(row: Int, col: Int): Boolean {
        val candy = board[row][col].candy ?: return false
        val type = candy.type
        
        // Check horizontal
        var horizontalCount = 1
        if (col >= 1 && board[row][col - 1].candy?.type == type) horizontalCount++
        if (col >= 2 && board[row][col - 2].candy?.type == type) horizontalCount++
        if (horizontalCount >= 3) return true
        
        // Check vertical
        var verticalCount = 1
        if (row >= 1 && board[row - 1][col].candy?.type == type) verticalCount++
        if (row >= 2 && board[row - 2][col].candy?.type == type) verticalCount++
        if (verticalCount >= 3) return true
        
        return false
    }
    
    fun getTile(row: Int, col: Int): Tile? {
        return if (row >= 0 && row < rows && col >= 0 && col < cols) {
            board[row][col]
        } else null
    }
    
    fun getBoard(): Array<Array<Tile>> = board
    
    fun swapCandies(pos1: Point, pos2: Point): Boolean {
        if (!matchDetector.canSwap(board, pos1, pos2)) {
            return false
        }
        
        val tile1 = board[pos1.y][pos1.x]
        val tile2 = board[pos2.y][pos2.x]
        
        val temp = tile1.candy
        tile1.candy = tile2.candy
        tile2.candy = temp
        
        // Update positions for animation
        tile1.candy?.targetPosition = PointF(pos1.x.toFloat(), pos1.y.toFloat())
        tile2.candy?.targetPosition = PointF(pos2.x.toFloat(), pos2.y.toFloat())
        
        return true
    }
    
    fun findMatches(): List<Match> {
        return matchDetector.findAllMatches(board)
    }
    
    fun removeMatches(matches: List<Match>): List<Point> {
        val removedPositions = mutableListOf<Point>()
        val specialCandyPositions = mutableListOf<Point>()
        
        // Create special candies first
        for (match in matches) {
            if (match.isSpecialMatch()) {
                specialCandySystem.createSpecialCandy(match, board)?.let {
                    specialCandyPositions.add(it)
                }
            }
        }
        
        // Remove matched tiles (except special candy positions)
        for (match in matches) {
            for (pos in match.tiles) {
                if (pos !in specialCandyPositions) {
                    board[pos.y][pos.x].clear()
                    removedPositions.add(pos)
                }
            }
        }
        
        return removedPositions
    }
    
    fun applyGravity(tileSize: Float): Boolean {
        return gravitySystem.applyGravity(board, tileSize)
    }
    
    fun updateAnimations(speed: Float): Boolean {
        return gravitySystem.updateAnimations(board, speed)
    }
    
    fun activateSpecialCandy(pos: Point): List<Point> {
        return specialCandySystem.activateSpecialCandy(board, pos)
    }
    
    fun hasValidMoves(): Boolean {
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                // Try swapping with right neighbor
                if (col < cols - 1) {
                    if (matchDetector.canSwap(board, Point(col, row), Point(col + 1, row))) {
                        return true
                    }
                }
                // Try swapping with bottom neighbor
                if (row < rows - 1) {
                    if (matchDetector.canSwap(board, Point(col, row), Point(col, row + 1))) {
                        return true
                    }
                }
            }
        }
        return false
    }
    
    fun getRows(): Int = rows
    fun getCols(): Int = cols
}
