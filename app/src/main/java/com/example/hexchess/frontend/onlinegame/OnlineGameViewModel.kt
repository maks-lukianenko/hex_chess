package com.example.hexchess.frontend.onlinegame

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.hexchess.backend.onlinegame.Piece
import com.example.hexchess.backend.onlinegame.Position


private const val TAG = "Online Game View Model"


class OnlineGameViewModel() : ViewModel() {
    var availableMoves = mutableStateListOf<Position?>(null)
    var cells = mutableStateListOf(mutableStateListOf<Piece?>(null))
    var chosenPosition: Position? = null
    var promotionTarget: Position? = null
    var isPromotion by mutableStateOf(false)
    var isEnumeration by mutableStateOf(false)


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