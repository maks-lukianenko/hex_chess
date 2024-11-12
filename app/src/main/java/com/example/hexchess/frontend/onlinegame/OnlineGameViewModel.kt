package com.example.hexchess.frontend.onlinegame

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.hexchess.backend.onlinegame.Piece
import com.example.hexchess.backend.onlinegame.Position


private const val TAG = "Online Game View Model"


class OnlineGameViewModel() : ViewModel() {
    var availableMoves = mutableStateListOf<Position?>(null)
    var cells = mutableStateListOf(mutableStateListOf<Piece?>(null))
    val color = "black"



    fun boardUpdate(boardState : MutableList<MutableList<Piece?>>) {
        cells.clear()
        boardState.forEachIndexed { columnIndex, columnCells ->
            cells.add(mutableStateListOf())
            cells[columnIndex].addAll(columnCells)
        }
    }

    fun updateAvailableMoves(positionList: Collection<Position?>) {
        availableMoves.clear()
        availableMoves.addAll(positionList)
    }
}