package com.example.hexchess.backend.onlinegame

class Position(val x: Int, var y: Int, private val withOffset: Boolean = true) {

    init {
        if (x > 5 && withOffset) y += x - 5
    }
    operator fun component1(): Int {
        return x
    }
    operator fun component2(): Int {
        return y
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Position) return false

        return x == other.x && y == other.y
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    fun getWithoutOffset() : Pair<Int, Int> {
        return if (x > 5 && withOffset) Pair(x, y - x + 5) else Pair(x, y)
    }
}