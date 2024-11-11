package com.example.hexchess.frontend.onlinegame

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.hexchess.backend.gamemanager.GameManager
import com.example.hexchess.backend.onlinegame.Board
import com.example.hexchess.backend.onlinegame.Piece
import com.example.hexchess.backend.onlinegame.Position


private const val TAG = "Online Game View Model"


class OnlineGameViewModel() : ViewModel() {
    var availableMoves = mutableStateListOf<Position?>(null)
    var cells = mutableStateListOf(mutableStateListOf<Piece?>(null))
    val color = "white"
    private val gameManager = GameManager("test")

    init {
        gameManager.connectToGame("a116c6")
    }


    fun boardUpdate() {
        val boardState = gameManager.getBoardState()
        cells.clear()
        boardState.forEachIndexed { columnIndex, columnCells ->
            cells.add(mutableStateListOf())
            cells[columnIndex].addAll(columnCells)
        }
    }

    fun makeMove(from: Position?, target: Position) {
        if (from != null) {
            val (fx, fy) = from.getWithoutOffset()
            val (tx, ty) = target.getWithoutOffset()
            gameManager.sendMove(fx, fy, tx, ty)
            boardUpdate()
            availableMoves.clear()
        }
    }

    fun getAvailableMoves(position: Position) {
        availableMoves.clear()
        availableMoves.addAll(gameManager.getAvailableMoves(position))
    }
}