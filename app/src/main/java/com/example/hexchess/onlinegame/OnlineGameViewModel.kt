package com.example.hexchess.onlinegame

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.hexchess.backend.onlinegame.Board
import com.example.hexchess.backend.onlinegame.Piece
import com.example.hexchess.backend.onlinegame.Position


private const val TAG = "View Model"


class OnlineGameViewModel : ViewModel() {
    var cells = mutableStateListOf(mutableStateListOf<Piece?>(null))
    var board = Board()
    val color = "black"


    fun boardUpdate() {
//        board.initializeHexBoard()
//        board.setupHexBoard()
        cells.clear()
        board.cells.forEachIndexed { columnIndex, columnCells ->
            cells.add(mutableStateListOf())
            cells[columnIndex].addAll(columnCells)
        }
    }

    fun makeMove(from: Position?, target: Position) {
        if (from != null) {
            board.cells[target.x][target.y]  = board.cells[from.x][from.y]
            board.cells[from.x][from.y] = null
            if (board.cells[target.x][target.y] != null && board.cells[from.x][from.y] == null) {
                Log.d(TAG, "Writed correctly")
            }
            boardUpdate()
        }
    }

}