package com.example.hexchess.frontend.onlinegame

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hexchess.R
import com.example.hexchess.backend.onlinegame.PieceColor

@Composable
fun PlayerStatus(
    nickname: String,
    rating: String,
    isPlayerTurn: Boolean,
    playerColor: PieceColor,
    capturedPieces: List<Int>,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween // Ensures proper spacing
    ) {
        // Player nickname
        Text(
            text = if (isPlayerTurn) "\u2794 $nickname ($rating)" else "$nickname ($rating)",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(end = 8.dp),
            fontSize = 14.sp,
        )

        // Captured pieces
        CapturedPiecesRow(
            capturedPieces = capturedPieces,
            color = playerColor,
            modifier = Modifier.wrapContentWidth()
        )
    }
}

@Composable
fun CapturedPiecesRow(
    capturedPieces: List<Int>,
    color: PieceColor,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy((-11).dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val sortedPieces = capturedPieces.sorted()
        sortedPieces.forEach { piece ->
            val imageResId = when (piece) {
                1 -> if (color == PieceColor.Black) R.drawable.pawn_white else R.drawable.pawn_dark
                9 -> if (color == PieceColor.Black) R.drawable.queen_white else R.drawable.queen_dark
                5 -> if (color == PieceColor.Black) R.drawable.rook_white else R.drawable.rook_dark
                2 -> if (color == PieceColor.Black) R.drawable.bishop_white else R.drawable.bishop_dark
                3 -> if (color == PieceColor.Black) R.drawable.knight_white else R.drawable.knight_dark
                else -> { R.drawable.empty }
            }
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = null, // Use proper descriptions for accessibility
                modifier = Modifier
                    .size(24.dp) // Adjust size as needed
            )
        }
    }
}