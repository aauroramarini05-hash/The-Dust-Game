package com.xdustatom.auryxsoda

import android.graphics.Point

data class Match(
    val tiles: List<Point>,
    val type: CandyType,
    val isHorizontal: Boolean
) {
    val size: Int get() = tiles.size

    fun isSpecialMatch(): Boolean = size >= 4

    fun getSpecialType(): CandySpecial {
        return when {
            size >= 5 -> CandySpecial.BOMB
            size == 4 -> if (isHorizontal) CandySpecial.LINE_HORIZONTAL else CandySpecial.LINE_VERTICAL
            else -> CandySpecial.NONE
        }
    }
}
