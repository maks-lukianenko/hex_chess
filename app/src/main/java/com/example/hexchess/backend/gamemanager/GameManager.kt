package com.example.hexchess.backend.gamemanager
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.hexchess.R
import com.example.hexchess.backend.onlinegame.Board
import com.example.hexchess.backend.onlinegame.Piece
import com.example.hexchess.backend.onlinegame.PieceColor
import com.example.hexchess.backend.onlinegame.PieceType
import com.example.hexchess.backend.onlinegame.Position
import  okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONObject

private const val TAG = "Game Manager"

class GameManager {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    var isWaiting by mutableStateOf(true)
    var isConnected by mutableStateOf(true)
    var isWin by mutableStateOf(false)
    var isEndedGame by mutableStateOf(false)
    var token: String? = ""
    var username: String? = ""
    var opponent = ""
    val board = Board()
    var color = "white"
    var isPlayerTurn by mutableStateOf(false)
    val whiteCapturedPieces = mutableStateListOf<Int>()
    val blackCapturedPieces = mutableStateListOf<Int>()
    val currentMove = mutableStateListOf<Position>()
    var currentRating = ""
    var opponentRating = ""

    fun connectToGame(context: Context) {
        val request = Request.Builder().url("ws://${context.getString(R.string.server_ip)}/ws/games/match/")
            .addHeader("Authorization", "Bearer $token")
            .build()
        resetGame()
        webSocket = client.newWebSocket(request, ChessWebSocketListener())
    }

    fun disconnect() {
        webSocket?.let {
            it.close(1000, "User disconnected")
            Log.d(TAG, "Disconnected from WebSocket.")
        }
        webSocket = null
    }

    fun sendMove(fx: Int, fy: Int, tx: Int, ty: Int) {
        val moveJson = JSONObject().apply {
            put("type", "make_move")
            put("from", coordsToChessNotation(fx, fy))
            put("to", coordsToChessNotation(tx, ty))
        }
        webSocket?.send(moveJson.toString())
    }

    fun sendPromotion(fx: Int, fy: Int, tx: Int, ty: Int, piece: String) {
        val moveJson = JSONObject().apply {
            put("type", "make_promotion")
            put("from", coordsToChessNotation(fx, fy))
            put("to", coordsToChessNotation(tx, ty))
            put("piece", piece)
        }
        webSocket?.send(moveJson.toString())
    }

    fun sendCheckMate() {
        val moveJson = JSONObject().apply {
            put("type", "checkmate")
        }
        Log.d(TAG, "Checkmate sent")
        webSocket?.send(moveJson.toString())
    }

    private inner class ChessWebSocketListener : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
            Log.d(TAG, "Connected to WebSocket!")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val data = JSONObject(text)
            when (data.getString("type")) {
                "error" -> {
                    Log.d(TAG, "Error: ${data.getString("message")}")
                }
                "move_made" -> {
                    val from = data.getString("from")
                    val to = data.getString("to")
                    Log.d(TAG, "Moved piece from $from to $to")
                    updateState(from, to)
                }
                "promotion_made" -> {
                    val from = data.getString("from")
                    val to = data.getString("to")
                    val piece = data.getString("piece")
                    updateState(from, to, piece)
                }
                "game_waiting" -> {
                    isWaiting = true
                    Log.d(TAG, "Waiting for opponent")
                }
                "game_start" -> {
                    isWaiting = false
                    color = data.getString("color")
                    opponent = data.getString("opponent")
                    board.setKingPosition(data.getString("color"))
                    isPlayerTurn = color == "white"
                    currentRating = data.getString("current_score")
                    opponentRating = data.getString("opponent_score")

                    Log.d(TAG, "Opponent found")
                    Log.d(TAG, "Your color is ${data.getString("color")}")
                }
                "turn_update" -> {
                    isPlayerTurn = data.getString("current_turn") == color
                    Log.d(TAG, "TURN: ${data.getString("current_turn")}")
                }
                "opponent_disconnected" -> {
                    isConnected=false
                    Log.d(TAG, "Opponent disconnected")
                }
                "win" -> {
                    isEndedGame = true
                    isWin = true
                    Log.d(TAG, "You won")
                }
                "lose" -> {
                    isEndedGame = true
                    isWin = false
                    Log.d(TAG, "You loose")
                }
            }
            Log.d(TAG, "ON MESSAGE")
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            println("Receiving bytes: $bytes")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(1000, null)
            Log.d(TAG, "Closing: $code / $reason")
            isConnected = false
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
            isConnected = false
            Log.d(TAG, "Error: ${t.message}")
        }
    }


    private fun updateState(from: String, to: String, piecePromotion: String = "") {
        currentMove.clear()
        val (fx, fy) = chessNotationToCoords(from)
        val (tx, ty) = chessNotationToCoords(to)
        val pieceColor = board.cells[fx][fy]!!.color
        val playerColor = if (color == "white") PieceColor.White else PieceColor.Black

        currentMove.add(Position(fx, fy))
        currentMove.add(Position(tx, ty))

        if (board.cells[tx][ty] != null) {
            if (board.cells[tx][ty]!!.color == PieceColor.White) {
                when (board.cells[tx][ty]!!.type) {
                    PieceType.Pawn -> whiteCapturedPieces.add(1)
                    PieceType.King -> whiteCapturedPieces.add(12)
                    PieceType.Queen -> whiteCapturedPieces.add(9)
                    PieceType.Rook -> whiteCapturedPieces.add(5)
                    PieceType.Bishop -> whiteCapturedPieces.add(2)
                    PieceType.Knight -> whiteCapturedPieces.add(3)
                }
            } else {
                when (board.cells[tx][ty]!!.type) {
                    PieceType.Pawn -> blackCapturedPieces.add(1)
                    PieceType.King -> blackCapturedPieces.add(12)
                    PieceType.Queen -> blackCapturedPieces.add(9)
                    PieceType.Rook -> blackCapturedPieces.add(5)
                    PieceType.Bishop -> blackCapturedPieces.add(2)
                    PieceType.Knight -> blackCapturedPieces.add(3)
                }
            }
        }

        if (piecePromotion == "") {
            board.cells[fx][fy]!!.isFirstTurn = false
            board.cells[tx][ty] = board.cells[fx][fy]
            if (board.cells[tx][ty]!!.type == PieceType.King && pieceColor == playerColor) board.kingPosition = Position(tx, ty)
            board.cells[fx][fy] = null
        } else {
            val pieceType: PieceType = when (piecePromotion) {
                "rook" -> PieceType.Rook
                "bishop" -> PieceType.Bishop
                "queen" -> PieceType.Queen
                "knight" -> PieceType.Knight
                else -> {PieceType.Pawn}
            }
            board.cells[tx][ty] = Piece(pieceColor, pieceType, false)
            board.cells[fx][fy] = null
        }
        if (playerColor != pieceColor && board.checkForCheckMate()) sendCheckMate()
    }

    private fun chessNotationToCoords(pos: String): Pair<Int, Int> {
        val column = pos[0]
        val row = pos.substring(1).toInt()
        val x = column - 'a'
        val y = row - 1
        return Pair(x, y)
    }

    private fun coordsToChessNotation(x: Int, y: Int): String {
        val column = ('a' + x).toString()
        val row = (y + 1).toString()

        return column + row
    }

    fun getAvailableMoves(position: Position): Collection<Position?> {
        return board.getAvailableMoves(position)
    }

    private fun resetGame() {
        board.resetBoard()
        currentMove.clear()
        whiteCapturedPieces.clear()
        blackCapturedPieces.clear()
        currentRating = ""
        isWin = false
        isEndedGame = false
        isConnected = true
    }
}