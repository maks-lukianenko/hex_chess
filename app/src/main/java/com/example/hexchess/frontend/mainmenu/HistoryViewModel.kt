package com.example.hexchess.frontend.mainmenu

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hexchess.R
import com.example.hexchess.backend.gamemanager.Game
import com.example.hexchess.backend.onlinegame.Piece
import com.example.hexchess.backend.onlinegame.PieceColor
import com.example.hexchess.backend.onlinegame.PieceType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

class HistoryViewModel : ViewModel() {
    private val _games = MutableStateFlow<List<Game>>(emptyList())
    private var _cells: MutableList<MutableList<Piece?>> = mutableListOf()
    val games: StateFlow<List<Game>> = _games
    var cells = mutableStateListOf(mutableStateListOf<Piece?>(null))
    var currentGame = -1

    private val client = OkHttpClient()

    fun resetBoard() {
        _cells = mutableListOf()
        for (i in 6..11) {
            _cells.add(MutableList(i) { null })
        }

        for (i in 10 downTo 6) {
            _cells.add(MutableList(i) { null })
        }
        setupHexBoard()
        boardUpdate()
    }

    private fun boardUpdate() {
        cells.clear()
        _cells.forEachIndexed { columnIndex, columnCells ->
            cells.add(mutableStateListOf())
            cells[columnIndex].addAll(columnCells)
        }
    }

    private fun setupHexBoard() {
        for (i in 1..5) {
            _cells[i][6] = Piece(PieceColor.Black, PieceType.Pawn, true)
            _cells[10 - i][6] = Piece(PieceColor.Black, PieceType.Pawn, true)
            _cells[i][i - 1] = Piece(PieceColor.White, PieceType.Pawn, true)
            _cells[10 - i][i - 1] = Piece(PieceColor.White, PieceType.Pawn, true)
        }

        _cells[2][0] = Piece(PieceColor.White, PieceType.Rook, true)
        _cells[8][0] = Piece(PieceColor.White, PieceType.Rook, true)
        _cells[2][7] = Piece(PieceColor.Black, PieceType.Rook, true)
        _cells[8][7] = Piece(PieceColor.Black, PieceType.Rook, true)

        _cells[3][0] = Piece(PieceColor.White, PieceType.Knight, true)
        _cells[7][0] = Piece(PieceColor.White, PieceType.Knight, true)
        _cells[3][8] = Piece(PieceColor.Black, PieceType.Knight, true)
        _cells[7][8] = Piece(PieceColor.Black, PieceType.Knight, true)

        _cells[4][0] = Piece(PieceColor.White, PieceType.Queen, true)
        _cells[6][0] = Piece(PieceColor.White, PieceType.King, true)
        _cells[4][9] = Piece(PieceColor.Black, PieceType.Queen, true)
        _cells[6][9] = Piece(PieceColor.Black, PieceType.King, true)

        _cells[5][0] = Piece(PieceColor.White, PieceType.Bishop, true)
        _cells[5][1] = Piece(PieceColor.White, PieceType.Bishop, true)
        _cells[5][2] = Piece(PieceColor.White, PieceType.Bishop, true)
        _cells[5][8] = Piece(PieceColor.Black, PieceType.Bishop, true)
        _cells[5][9] = Piece(PieceColor.Black, PieceType.Bishop, true)
        _cells[5][10] = Piece(PieceColor.Black, PieceType.Bishop, true)
    }

    fun fetchGames(token: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val url = "http://${context.getString(R.string.server_ip)}/game-manager/games/"

            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $token")
                .build()

            try {
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    response.body?.string()?.let { responseBody ->
                        val gameListType = object : TypeToken<List<Game>>(){}.type
                        Log.d("MainMenuViewModel", responseBody)
                        val games: List<Game> = Gson().fromJson(responseBody, gameListType)
                        _games.value = games
                    }
                } else {
                    Log.d("MainMenuViewModel", "Error: ${response.code}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}