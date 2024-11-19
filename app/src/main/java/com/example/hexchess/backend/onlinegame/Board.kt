package com.example.hexchess.backend.onlinegame

import android.util.Log

private val TAG = "BOARD"

class Board {
    lateinit var cells: MutableList<MutableList<Piece?>>

    init {
        initializeHexBoard()
        setupHexBoard()
    }

    fun resetBoard() {
        cells.clear()
        initializeHexBoard()
        setupHexBoard()
    }

    private fun initializeHexBoard() { // Init classic hex bord with 71 cells
        cells = mutableListOf()
        for (i in 6..11) {
            cells.add(MutableList(i) { null })
        }

        for (i in 10 downTo 6) {
            cells.add(MutableList(i) { null })
        }
    }

    private fun setupHexBoard() {
        for (i in 1..5) {
            cells[i][6] = Piece(PieceColor.Black, PieceType.Pawn, true)
            cells[10 - i][6] = Piece(PieceColor.Black, PieceType.Pawn, true)
            cells[i][i - 1] = Piece(PieceColor.White, PieceType.Pawn, true)
            cells[10 - i][i - 1] = Piece(PieceColor.White, PieceType.Pawn, true)
        }

        cells[2][0] = Piece(PieceColor.White, PieceType.Rook, true)
        cells[8][0] = Piece(PieceColor.White, PieceType.Rook, true)
        cells[2][7] = Piece(PieceColor.Black, PieceType.Rook, true)
        cells[8][7] = Piece(PieceColor.Black, PieceType.Rook, true)

        cells[3][0] = Piece(PieceColor.White, PieceType.Knight, true)
        cells[7][0] = Piece(PieceColor.White, PieceType.Knight, true)
        cells[3][8] = Piece(PieceColor.Black, PieceType.Knight, true)
        cells[7][8] = Piece(PieceColor.Black, PieceType.Knight, true)

        cells[4][0] = Piece(PieceColor.White, PieceType.Queen, true)
        cells[6][0] = Piece(PieceColor.White, PieceType.King, true)
        cells[4][9] = Piece(PieceColor.Black, PieceType.Queen, true)
        cells[6][9] = Piece(PieceColor.Black, PieceType.King, true)

        cells[5][0] = Piece(PieceColor.White, PieceType.Bishop, true)
        cells[5][1] = Piece(PieceColor.White, PieceType.Bishop, true)
        cells[5][2] = Piece(PieceColor.White, PieceType.Bishop, true)
        cells[5][8] = Piece(PieceColor.Black, PieceType.Bishop, true)
        cells[5][9] = Piece(PieceColor.Black, PieceType.Bishop, true)
        cells[5][10] = Piece(PieceColor.Black, PieceType.Bishop, true)

    }

    private fun isValidMove(position: Position, color: PieceColor) : Boolean {
        val x = position.x
        val y = if (x < 6) position.y else position.y - x + 5
        if (x !in cells.indices || y !in cells[x].indices) return false
        if (cells[x][y] == null || cells[x][y]?.color != color) return true
        return false
    }

    private fun isPawnCapture(position: Position, color: PieceColor) : Boolean {
        val x = position.x
        val y = if (x < 6) position.y else position.y - x + 5
        if (x !in cells.indices || y !in cells[x].indices) return false
        if (cells[x][y] != null && cells[x][y]?.color != color) return true
        return false
    }

    fun getAvailableMoves(position: Position): List<Position> {
        val (x, y) = position.getWithoutOffset()
        val piece = cells[x][y] ?: return emptyList()
        val availableMoves = mutableListOf<Position>()

        when (piece.type) {
            PieceType.Pawn -> availableMoves.addAll(getPawnMoves(position, piece.color))
            PieceType.Bishop -> availableMoves.addAll(getBishopMoves(position, piece.color))
            PieceType.Rook -> availableMoves.addAll(getRookMoves(position, piece.color))
            PieceType.Knight -> availableMoves.addAll(getKnightMoves(position, piece.color))
            PieceType.Queen -> {
                availableMoves.addAll(getBishopMoves(position, piece.color))
                availableMoves.addAll(getRookMoves(position, piece.color))
            }

            PieceType.King -> availableMoves.addAll(getKingMoves(position, piece.color))
        }
        return availableMoves
    }

    private fun getKingMoves(position: Position, color: PieceColor): Collection<Position> {
        val (x, y) = position
        return listOf(
            Position(x, y + 1, false),
            Position(x + 1, y + 1, false),
            Position(x + 1, y, false),
            Position(x,y - 1, false),
            Position(x - 1,y - 1, false),
            Position(x - 1, y, false),
            Position( x - 1, y + 1, false),
            Position(x + 1, y + 2, false),
            Position(x + 2, y + 1, false),
            Position(x + 1, y - 1, false),
            Position(x - 1, y - 2, false),
            Position(x - 2, y - 1, false)
        ).filter { elem -> isValidMove(elem, color) && !isBlockedField(elem, color) }
    }

    private fun getKnightMoves(position: Position, color: PieceColor): Collection<Position> {
        val (x, y) = position
        return listOf(
            Position(x + 1, y + 3, withOffset = false),
            Position(x + 2, y + 3, withOffset = false),
            Position(x + 3, y + 2, withOffset = false),
            Position(x + 3, y + 1, withOffset = false),
            Position(x + 2 ,y - 1, withOffset = false),
            Position(x + 1, y - 2, withOffset = false),
            Position(x - 1, y - 3, withOffset = false),
            Position(x - 2, y - 3, withOffset = false),
            Position(x - 3, y - 2, withOffset = false),
            Position(x - 3, y - 1, withOffset = false),
            Position(x - 2, y + 1, withOffset = false),
            Position(x - 1, y + 2, withOffset = false),
        ).filter { elem -> isValidMove(elem, color) }

    }

    private fun getRookMoves(position: Position, color: PieceColor): Collection<Position> {
        val availableMoves = mutableListOf<Position>()
        val (x, y) = position

        listOf(
            Pair(0, 1), Pair(1, 1), Pair(1, 0), Pair(0, -1), Pair(-1, -1), Pair(-1, 0)
        ).forEach { (dx, dy) ->
            var nx = x + dx
            var ny = y + dy
            while (isValidMove(Position(nx, ny, false), color)) {
                val noy = if (nx > 5) ny - nx + 5 else ny
                availableMoves.add(Position(nx, ny, false))
                if (cells[nx][noy] != null) break
                nx += dx
                ny += dy
            }
        }
        return availableMoves
    }

    private fun getBishopMoves(position: Position, color: PieceColor): Collection<Position> {
        val (x, y) = position
        val availableMoves = mutableListOf<Position>()

        listOf(
            Pair(1, 2), Pair(2, 1), Pair(1, -1), Pair(-1, -2), Pair(-2, -1), Pair(-1, 1)
        ).forEach { (dx, dy) ->
            var nx = x + dx
            var ny = y + dy
            while (isValidMove(Position(nx, ny, false), color)) {
                val noy = if (nx > 5) ny - nx + 5 else ny
                availableMoves.add(Position(nx, ny, false))
                if (cells[nx][noy] != null) break
                nx += dx
                ny += dy
            }
        }
        return availableMoves
    }

    private fun getPawnMoves(position: Position, color: PieceColor): Collection<Position> {
        val availableMoves = mutableListOf<Position>()
        val (x, y) = position.getWithoutOffset()
        val (wx, wy) = position
        val boardHeight = cells[x].size

        if (color == PieceColor.Black) {
            if (y > 0 && cells[x][y - 1] == null) {
                availableMoves.add(Position(x, y - 1))
                if (y > 1 && cells[x][y - 2] == null && cells[x][y]!!.isFirstTurn) availableMoves.add(Position(x, y - 2))
            }
            if (isPawnCapture(Position(wx - 1, wy - 1, false), color)) availableMoves.add(Position(wx - 1, wy - 1, false))
            if (isPawnCapture(Position(wx + 1, wy, false), color)) availableMoves.add(Position(wx + 1, wy, false))
        } else {
            if (y < boardHeight - 1 && cells[x][y + 1] == null) {
                availableMoves.add(Position(x, y +  1))
                if (y < boardHeight - 2 && cells[x][y + 2] == null && cells[x][y]!!.isFirstTurn) availableMoves.add(
                    Position(x, y + 2)
                )
            }
            if (isPawnCapture(Position(wx - 1, wy, false), color)) availableMoves.add(Position(wx - 1, wy, false))
            if (isPawnCapture(Position(wx + 1, wy + 1, false), color)) availableMoves.add(Position(wx + 1, wy + 1, false))
        }
        return availableMoves
    }

    private fun isPawnField(position: Position, color: PieceColor) : Boolean {
        val x = position.x
        val y = if (x < 6) position.y else position.y - x + 5
        if (x !in cells.indices || y !in cells[x].indices) return false
        if (cells[x][y] != null && cells[x][y]?.color != color && cells[x][y]?.type == PieceType.Pawn) return true
        return false
    }

    private fun blockDefendingPieces() {
        // TODO use function isBlockedField for check if after piece move King can't be captured
    }

    private fun isBlockedField(position: Position, color: PieceColor) : Boolean {
        val (x, y) = position

        // Check enemy pawns block
        if (color == PieceColor.White) {
            if (isPawnField(Position(x - 1, y, false), color) || isPawnField(Position(x + 1, y + 1, false), color)) return true
        } else {
            if (isPawnField(Position(x - 1, y - 1, false), color) || isPawnField(Position(x + 1, y, false), color)) return true
        }

        // Check bishop and queen block
        listOf(
            Pair(1, 2), Pair(2, 1), Pair(1, -1), Pair(-1, -2), Pair(-2, -1), Pair(-1, 1)
        ).forEach { (dx, dy) ->
            var nx = x + dx
            var ny = y + dy
            var noy = if (nx > 5) ny - nx + 5 else ny
            var firstIter = true
            while (nx in cells.indices && noy in cells[nx].indices) {
                if (cells[nx][noy] != null) {
                    if (cells[nx][noy]!!.color == color) break
                    if (firstIter && cells[nx][noy]!!.type == PieceType.King) return true
                    if (cells[nx][noy]!!.type == PieceType.Bishop || cells[nx][noy]!!.type == PieceType.Queen) return true else break
                }
                firstIter = false
                nx += dx
                ny += dy
                noy = if (nx > 5) ny - nx + 5 else ny
            }
        }

        // Check rook and queen block
        listOf(
            Pair(0, 1), Pair(1, 1), Pair(1, 0), Pair(0, -1), Pair(-1, -1), Pair(-1, 0)
        ).forEach { (dx, dy) ->
            var nx = x + dx
            var ny = y + dy
            var noy = if (nx > 5) ny - nx + 5 else ny
            var firstIter = true
            while (nx in cells.indices && noy in cells[nx].indices) {
                if (cells[nx][noy] != null) {
                    if (cells[nx][noy]!!.color == color ) break
                    if (firstIter && cells[nx][noy]!!.type == PieceType.King) return true
                    if (cells[nx][noy]!!.type == PieceType.Rook || cells[nx][noy]!!.type == PieceType.Queen) return true else break
                }
                firstIter = false
                nx += dx
                ny += dy
                noy = if (nx > 5) ny - nx + 5 else ny
            }
        }

        // Check knight block
        val possibleKnightPositions = listOf(
            Position(x + 1, y + 3, withOffset = false),
            Position(x + 2, y + 3, withOffset = false),
            Position(x + 3, y + 2, withOffset = false),
            Position(x + 3, y + 1, withOffset = false),
            Position(x + 2 ,y - 1, withOffset = false),
            Position(x + 1, y - 2, withOffset = false),
            Position(x - 1, y - 3, withOffset = false),
            Position(x - 2, y - 3, withOffset = false),
            Position(x - 3, y - 2, withOffset = false),
            Position(x - 3, y - 1, withOffset = false),
            Position(x - 2, y + 1, withOffset = false),
            Position(x - 1, y + 2, withOffset = false),
        )

        possibleKnightPositions.forEach { elem ->
            val nx = elem.x
            val ny = if (nx > 5) elem.y - nx + 5 else elem.y
            if (nx in cells.indices && ny in cells[nx].indices) {
                if (cells[nx][ny] != null && cells[nx][ny]?.color != color && cells[nx][ny]?.type == PieceType.Knight) return true
            }
        }

        return false
    }
}