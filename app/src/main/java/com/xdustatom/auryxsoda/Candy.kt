package com.xdustatom.auryxsoda

import android.graphics.PointF

enum class CandyType {
    RED, BLUE, GREEN, YELLOW, PURPLE, ORANGE, EMPTY
}

enum class CandySpecial {
    NONE, LINE_HORIZONTAL, LINE_VERTICAL, BOMB
}

data class Candy(
    var type: CandyType,
    var special: CandySpecial = CandySpecial.NONE,
    var position: PointF = PointF(0f, 0f),
    var targetPosition: PointF = PointF(0f, 0f),
    var isAnimating: Boolean = false,
    var alpha: Float = 1f,
    var scale: Float = 1f,
    var rotation: Float = 0f
) {
    fun startMoveTo(target: PointF) {
        targetPosition = PointF(target.x, target.y)
        isAnimating = true
    }

    fun updateAnimation(speed: Float): Boolean {
        if (!isAnimating) return false

        val dx = targetPosition.x - position.x
        val dy = targetPosition.y - position.y
        val distance = Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()

        if (distance < speed) {
            position.set(targetPosition.x, targetPosition.y)
            isAnimating = false
            return true
        }

        position.x += dx / distance * speed
        position.y += dy / distance * speed
        return false
    }

    fun isEmpty(): Boolean = type == CandyType.EMPTY

    fun reset() {
        alpha = 1f
        scale = 1f
        rotation = 0f
        isAnimating = false
    }
}
