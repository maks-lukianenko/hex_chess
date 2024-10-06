package com.example.hexchess.backend.onlinegame

import android.util.Log

class Board {
    lateinit var cells: MutableList<MutableList<Piece?>>

    init {
        initializeHexBoard()
        setupHexBoard()
    }

    fun initializeBoard(size: Int) {
        cells = MutableList(size) { MutableList(size) { null } }
    }

    fun initializeHexBoard() { // Init classic hex bord with 91 cells
        cells = mutableListOf()
        for (i in 6..11) {
            cells.add(MutableList(i) { null })
        }

        for (i in 10 downTo 6) {
            cells.add(MutableList(i) { null })
        }
    }

    fun setupHexBoard() {
        for (i in 1..5) {
            cells[i][6] = Piece(PieceColor.Black, PieceType.Pawn)
            cells[10 - i][6] = Piece(PieceColor.Black, PieceType.Pawn)
            cells[i][i - 1] = Piece(PieceColor.White, PieceType.Pawn)
            cells[10 - i][i - 1] = Piece(PieceColor.White, PieceType.Pawn)
        }

        cells[2][0] = Piece(PieceColor.White, PieceType.Rook)
        cells[8][0] = Piece(PieceColor.White, PieceType.Rook)
        cells[2][7] = Piece(PieceColor.Black, PieceType.Rook)
        cells[8][7] = Piece(PieceColor.Black, PieceType.Rook)

        cells[3][0] = Piece(PieceColor.White, PieceType.Knight)
        cells[7][0] = Piece(PieceColor.White, PieceType.Knight)
        cells[3][8] = Piece(PieceColor.Black, PieceType.Knight)
        cells[7][8] = Piece(PieceColor.Black, PieceType.Knight)

        cells[4][0] = Piece(PieceColor.White, PieceType.Queen)
        cells[6][0] = Piece(PieceColor.White, PieceType.King)
        cells[4][9] = Piece(PieceColor.Black, PieceType.Queen)
        cells[6][9] = Piece(PieceColor.Black, PieceType.King)

        cells[5][0] = Piece(PieceColor.White, PieceType.Bishop)
        cells[5][1] = Piece(PieceColor.White, PieceType.Bishop)
        cells[5][2] = Piece(PieceColor.White, PieceType.Bishop)
        cells[5][8] = Piece(PieceColor.Black, PieceType.Bishop)
        cells[5][9] = Piece(PieceColor.Black, PieceType.Bishop)
        cells[5][10] = Piece(PieceColor.Black, PieceType.Bishop)

    }

}