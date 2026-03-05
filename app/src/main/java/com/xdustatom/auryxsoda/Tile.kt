package com.xdustatom.auryxsoda

data class Tile(
    val row: Int,
    val col: Int,
    var candy: Candy? = null,
    var isLocked: Boolean = false
) {
    fun isEmpty(): Boolean = candy == null || candy?.isEmpty() == true

    fun clear() {
        candy = null
    }

    fun setCandy(newCandy: Candy) {
        candy = newCandy
    }
}
