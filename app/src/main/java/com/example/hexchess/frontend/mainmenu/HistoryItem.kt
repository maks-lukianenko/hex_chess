package com.example.hexchess.frontend.mainmenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.graphics.shapes.RoundedPolygon
import com.example.hexchess.R
import com.example.hexchess.backend.gamemanager.Game
import com.example.hexchess.backend.onlinegame.Piece
import com.example.hexchess.backend.onlinegame.PieceColor
import com.example.hexchess.backend.onlinegame.PieceType
import com.example.hexchess.utills.RoundedPolygonShape
import kotlin.math.sqrt

private val colors = arrayListOf(Color.DarkGray, Color.White, Color.LightGray)

@Composable
fun HistoryItem(game: Game, boardState: SnapshotStateList<SnapshotStateList<Piece?>>, onExitClick: () -> Unit) {
    val playerWhite = game.playerWhite
    val playerBlack = game.playerBlack
    var currentMoveID = 0

    val configuration = LocalConfiguration.current
    val SIZE = (configuration.screenWidthDp.dp / 10)
    val verticalPadding = (sqrt(3.0) / 4) * SIZE
    val horizontalPadding = 3 * SIZE / 4
    val centerX = configuration.screenWidthDp.dp / 2 - (horizontalPadding / 2)
    val startTop = 80.dp
    val cells = remember {
        boardState
    }
    Column {
        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 60.dp)) {
            IconButton(onClick = { onExitClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Exit"
                )
            }
            Text(text = "$playerWhite vs $playerBlack", textAlign = TextAlign.Center)
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(SIZE * 11.5f)
        ) {
            HistoryBoardColumn(
                sizeOfItem = SIZE,
                startColor = 0,
                top = startTop,
                start = centerX,
                columnCells = cells[5]
            )
            for (i in 1..5) {
                HistoryBoardColumn(
                    sizeOfItem = SIZE,
                    startColor = (6 - i),
                    top = startTop + (i * verticalPadding),
                    start = centerX + (i * horizontalPadding),
                    columnCells = cells[5 + i],
                )
                HistoryBoardColumn(
                    sizeOfItem = SIZE,
                    startColor = (6 - i),
                    top = startTop + (i * verticalPadding),
                    start = centerX - (i * horizontalPadding),
                    columnCells = cells[5 - i]
                )
            }
        }
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowLeft,
                    contentDescription = "Previous"
                )
            }
            IconButton(onClick = {
                if (currentMoveID in game.moves.indices) {
                    val move = game.moves[currentMoveID]
                    val (fx, fy) = chessNotationToCoords(move.from)
                    val (tx, ty) = chessNotationToCoords(move.to)
                    cells[tx][ty] = cells[fx][fy]
                    cells[fx][fy] = null
                    currentMoveID += 1
                }
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                    contentDescription = "Next"
                )
            }
        }

    }
}

@Composable
fun HistoryPieceView(
    sizeOfItem: Dp,
    color: Color,
    piece: Piece?,
) {

    val hexagon = remember {
        RoundedPolygon(6)
    }
    val clip = remember(hexagon) {
        RoundedPolygonShape(polygon = hexagon)
    }

    val imageResId = when (piece?.type) {
        PieceType.Pawn -> if (piece.color == PieceColor.White) R.drawable.pawn_white else R.drawable.pawn_dark
        PieceType.King -> if (piece.color == PieceColor.White) R.drawable.king_white else R.drawable.king_dark
        PieceType.Queen -> if (piece.color == PieceColor.White) R.drawable.queen_white else R.drawable.queen_dark
        PieceType.Rook -> if (piece.color == PieceColor.White) R.drawable.rook_white else R.drawable.rook_dark
        PieceType.Bishop -> if (piece.color == PieceColor.White) R.drawable.bishop_white else R.drawable.bishop_dark
        PieceType.Knight -> if (piece.color == PieceColor.White) R.drawable.knight_white else R.drawable.knight_dark
        null -> R.drawable.empty
    }

    Box (
        modifier = Modifier
            .border(1.dp, Color.Black, RoundedPolygonShape(polygon = hexagon))
            .background(
                color,
                RoundedPolygonShape(polygon = hexagon)
            )
            .clip(RoundedPolygonShape(polygon = hexagon))
            .size(sizeOfItem)
    ) {
        Image(
            painter = painterResource(imageResId),
            contentDescription = "Figure",
            modifier = Modifier
                .graphicsLayer {
                    this.shape = clip
                    this.clip = true
                }
                .size(sizeOfItem)
        )
    }

}

@Composable
fun HistoryBoardColumn (
    sizeOfItem: Dp,
    startColor: Int,
    top: Dp,
    start: Dp,
    columnCells: MutableList<Piece?>,
) {

    val columnSize = columnCells.size


    Column(
        modifier = Modifier
            .padding(start = start, top = top)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy((-7).dp)
    ) {
        // column indexes are from A to K( for us it's x )
        // y for us means row
        columnCells.forEachIndexed { i, _ ->
            HistoryPieceView(sizeOfItem, colors[(i + startColor) % 3], columnCells[columnSize - 1 - i])
        }
    }
}

private fun chessNotationToCoords(pos: String): Pair<Int, Int> {
    val column = pos[0]
    val row = pos.substring(1).toInt()
    val x = column - 'a'
    val y = row - 1
    return Pair(x, y)
}
