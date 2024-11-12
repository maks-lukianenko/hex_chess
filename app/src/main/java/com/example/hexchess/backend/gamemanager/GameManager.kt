package com.example.hexchess.backend.gamemanager
import android.util.Log
import com.example.hexchess.backend.onlinegame.Board
import com.example.hexchess.backend.onlinegame.Piece
import com.example.hexchess.backend.onlinegame.Position
import  okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONObject

private const val TAG = "Game Client"

class GameManager {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private val board = Board()

    fun connectToGame(gameCode: String) {
        val request = Request.Builder().url("ws://192.168.56.1:8000/ws/games/$gameCode/").build()
        webSocket = client.newWebSocket(request, ChessWebSocketListener())
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

    fun getBoardState() : MutableList<MutableList<Piece?>> {
        return board.cells
    }


    // WebSocket Listener to handle messages from the server
    private inner class ChessWebSocketListener : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
            Log.d(TAG, "Connected to WebSocket!")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val data = JSONObject(text)
            when {
                data.getString("type") == "error" -> {
                    Log.d(TAG, "Error: ${data.getString("message")}")
                }
                data.getString("type") == "move_made" -> {
                    val from = data.getString("from")
                    val to = data.getString("to")
                    Log.d(TAG, "Moved piece from $from to $to")
                    updateBoard(from, to)
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