package com.example.hexchess.frontend.onlinegame

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.hexchess.R

@Composable
fun PromotionRow(
    onPieceSelected: (String) -> Unit,
    color: String
) {
    val pieces = listOf("queen", "rook", "bishop", "knight") // List of pieces for promotion
    val pieceImages = if (color == "black") mapOf(
        "queen" to R.drawable.queen_dark,
        "rook" to R.drawable.rook_dark,
        "bishop" to R.drawable.bishop_dark,
        "knight" to R.drawable.knight_dark
    ) else mapOf(
        "queen" to R.drawable.queen_white,
        "rook" to R.drawable.rook_white,
        "bishop" to R.drawable.bishop_white,
        "knight" to R.drawable.knight_white
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        pieces.forEach { piece ->
            Image(
                painter = painterResource(id = pieceImages[piece] ?: R.drawable.empty),
                contentDescription = "$piece icon",
                modifier = Modifier
                    .size(60.dp)
                    .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
                        onPieceSelected(piece)
                    }
            )
        }
    }
}

@Composable
fun PromotionDialog(
    onPieceSelected: (String) -> Unit,
    onDismissRequest: () -> Unit,
    color: String
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(130.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Choose a piece for promotion",
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                PromotionRow(
                    onPieceSelected = { piece ->
                        onPieceSelected(piece)
                        onDismissRequest()
                    },
                    color = color
                )
            }
        }
    }
}