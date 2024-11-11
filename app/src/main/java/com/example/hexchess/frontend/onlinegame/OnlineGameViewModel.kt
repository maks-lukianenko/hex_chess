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
    private val board = Board()
    val color = "black"
    private val gameManager = GameManager("test")

    init {
        gameManager.connectToGame("a116c6")
    }


    fun boardUpdate() {
//        board.initializeHexBoard()
//        board.setupHexBoard()
//        val boardState = gameManager.getBoardState()
        cells.clear()
        board.cells.forEachIndexed { columnIndex, columnCells ->
            cells.add(mutableStateListOf())
            cells[columnIndex].addAll(columnCells)
        }
    }

    fun makeMove(from: Position?, target: Position) {
        if (from != null) {
            val (fx, fy) = from.getWithoutOffset()
            val (tx, ty) = target.getWithoutOffset()
            board.cells[tx][ty] = board.cells[fx][fy]
            board.cells[fx][fy] = null
            if (board.cells[tx][ty] != null && board.cells[fx][fy] == null) {
                Log.d(TAG, "Writed correctly")
            }
            gameManager.sendMove(fx, fy, tx, ty)
            boardUpdate()
            availableMoves.clear()
        }
    }

    fun getAvailableMoves(position: Position) {
        availableMoves.clear()
        availableMoves.addAll(board.getAvailableMoves(position))
    }
}