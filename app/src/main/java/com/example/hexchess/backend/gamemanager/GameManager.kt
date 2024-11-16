package com.example.hexchess.backend.gamemanager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.hexchess.backend.onlinegame.Board
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
    var token: String? = ""
    val board = Board()
    var color = "white"
    var isPlayerTurn = true

    fun connectToGame() {
        board.resetBoard()
        val request = Request.Builder().url("ws://192.168.56.1:8000/ws/games/match/")
            .addHeader("Authorization", "Bearer $token")
            .build()
        webSocket = client.newWebSocket(request, ChessWebSocketListener())
        isConnected = true
    }

    fun disconnect() {
        webSocket?.let {
            it.close(1000, "User disconnected")
            Log.d(TAG, "Disconnected from WebSocket.")
        }
        webSocket = null
    }

    fun sendMove(fx: Int, fy: Int, tx: Int, ty: Int, username: String) {
        val moveJson = JSONObject().apply {
            put("username", username)
            put("type", "make_move")
            put("from", coordsToChessNotation(fx, fy))
            put("to", coordsToChessNotation(tx, ty))
        }
        webSocket?.send(moveJson.toString())
    }


    // WebSocket Listener to handle messages from the server
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
                    updateBoard(from, to)
                }
                "game_waiting" -> {
                    isWaiting = true
                    Log.d(TAG, "Waiting for opponent")
                }
                "game_start" -> {
                    isWaiting = false
                    color = data.getString("color")
                    Log.d(TAG, "Opponent found")
                    Log.d(TAG, "Your color is ${data.getString("color")}")
                }
                "turn_update" -> {
                    isPlayerTurn = data.getString("current_turn") == color
                }
                "opponent_disconnected" -> {
                    isConnected=false
                    Log.d(TAG, "Opponent disconnected")
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
            Log.d(TAG, "Error: ${t.message}")
        }
    }


    private fun updateBoard(from: String, to: String) {
        val (fx, fy) = chessNotationToCoords(from)
        val (tx, ty) = chessNotationToCoords(to)

        // Move piece on the board
        board.cells[fx][fy]!!.isFirstTurn = false
        board.cells[tx][ty] = board.cells[fx][fy]
        board.cells[fx][fy] = null
    }

    private fun chessNotationToCoords(pos: String): Pair<Int, Int> {
        val column = pos[0]
        val row = pos[1].toString().toInt()
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
}