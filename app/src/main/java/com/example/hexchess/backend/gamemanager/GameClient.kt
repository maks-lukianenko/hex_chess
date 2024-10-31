package com.example.hexchess.backend.gamemanager
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONObject

private const val TAG = "Game Client"

class GameClient {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private val board: Array<Array<String?>> = Array(8) { arrayOfNulls<String>(8) }

    fun connectToGame(gameCode: String) {
        val request = Request.Builder().url("ws://YOUR_SERVER_URL/ws/games/$gameCode/").build()
        webSocket = client.newWebSocket(request, ChessWebSocketListener())
    }

    fun sendMove(from: String, to: String) {
        val moveJson = JSONObject().apply {
            put("from", from)
            put("to", to)
        }
        webSocket?.send(moveJson.toString())
    }

    // WebSocket Listener to handle messages from the server
    private inner class ChessWebSocketListener : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
            println("Connected to WebSocket!")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val data = JSONObject(text)
            if (data.has("error")) {
                println("Error: ${data.getString("error")}")
            } else if (data.has("from") && data.has("to")) {
                val from = data.getString("from")
                val to = data.getString("to")
                println("Opponent moved from $from to $to")
                updateBoard(from, to)
            }
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            println("Receiving bytes: $bytes")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(1000, null)
            println("Closing: $code / $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
            println("Error: ${t.message}")
        }
    }

    // Update board with the move received or made
    private fun updateBoard(from: String, to: String) {
        val (fx, fy) = chessNotationToCoords(from)
        val (tx, ty) = chessNotationToCoords(to)

        // Move piece on the board
        board[tx][ty] = board[fx][fy]
        board[fx][fy] = null
    }

    // Convert chess notation like "a2" to board indices like (6, 0)
    private fun chessNotationToCoords(pos: String): Pair<Int, Int> {
        val column = pos[0]
        val row = pos[1].toString().toInt()
        val x = 8 - row
        val y = column - 'a'
        return Pair(x, y)
    }
}