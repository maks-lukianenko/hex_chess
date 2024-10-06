package com.example.hexchess.onlinegame

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.RoundedPolygon
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.hexchess.R
import com.example.hexchess.utills.RoundedPolygonShape
import com.example.hexchess.backend.onlinegame.Piece
import com.example.hexchess.backend.onlinegame.PieceColor
import com.example.hexchess.backend.onlinegame.PieceType
import com.example.hexchess.backend.onlinegame.Position

private const val TAG = "Online game screen"
val viewModel: OnlineGameViewModel = OnlineGameViewModel()

@Composable
fun OnlineGameScreen(navController: NavHostController = rememberNavController()) {

    var chosenPosition by remember {
        mutableStateOf<Position?>(null)
    }

    val setPosition: (Position?) -> Unit = { position ->
        chosenPosition = position
    }

    viewModel.boardUpdate()
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xfff0e2b9))) {
        val configuration = LocalConfiguration.current
        val size = 45
        val radius = size / 2
        val verticalPadding = radius * Math.sqrt(3.0) / 2
        val horizontalPadding = 3 * size / 4
        val centerX = configuration.screenWidthDp.dp / 2 - (horizontalPadding / 2).dp
        val startTop = 50.dp
        val cells = remember {
            viewModel.cells
        }
        BoardColumn(columnIndex = 5, startColor = 0, top = startTop, start = centerX, size = size, columnCells = cells[5], setPosition, chosenPosition)
        for (i in 1..5) {
            BoardColumn(
                columnIndex = 5 + i,
                startColor = (6 - i),
                top = startTop + (i * verticalPadding).dp,
                start = centerX + (i * horizontalPadding).dp,
                size = size, columnCells = cells[5 + i],
                setPosition,
                chosenPosition
            )
            BoardColumn(
                columnIndex = 5 - i,
                startColor = (6 - i),
                top = startTop + (i * verticalPadding).dp,
                start = centerX - (i * horizontalPadding).dp,
                size = size,
                columnCells = cells[5 - i],
                setPosition,
                chosenPosition
            )
        }
    }
}

@Composable
fun PieceView(sizeOfItem: Dp, color: Color, piece: Piece?, x: Int, y: Int, setPosition: (Position?) -> Unit, chosenPosition: Position?) {
    // column indexes are from A to L( for us it's x )
    // y for us means row

    val hexagon = remember {
        RoundedPolygon(6)
    }
    val clip = remember(hexagon) {
        RoundedPolygonShape(polygon = hexagon)
    }
    var flag by remember { mutableStateOf(true) }

    val imageResId = when (piece?.type) {
        PieceType.Pawn -> if (piece.color == PieceColor.White) R.drawable.pawn_white else R.drawable.pawn_dark
        PieceType.King -> if (piece.color == PieceColor.White) R.drawable.king_white else R.drawable.king_dark
        PieceType.Queen -> if (piece.color == PieceColor.White) R.drawable.queen_white else R.drawable.queen_dark
        PieceType.Rook -> if (piece.color == PieceColor.White) R.drawable.rook_white else R.drawable.rook_dark
        PieceType.Bishop -> if (piece.color == PieceColor.White) R.drawable.bishop_white else R.drawable.bishop_dark
        PieceType.Knight -> if (piece.color == PieceColor.White) R.drawable.knight_white else R.drawable.knight_dark
        null -> R.drawable.empty
    }

    val position = Position(x, y)

    Box (
        modifier = Modifier
            .border(1.dp, Color.Black, RoundedPolygonShape(polygon = hexagon))
            .background(color, RoundedPolygonShape(polygon = hexagon))
            .size(sizeOfItem)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                Log.d(TAG, "BOX x:%d y:%d".format(position.x, position.y))
                if (chosenPosition != null) {
                    Log.d(TAG, "Chosen position x:%d y:%d".format(chosenPosition.x, chosenPosition.y))
                } else {
                    Log.d(TAG, "Chosen position is NULL")
                }
                viewModel.makeMove(chosenPosition, position)
                setPosition(null)
            }
    ) {
        if (piece != null) {
            Image(
                painter = painterResource( // TODO create function for this
                    imageResId
                ),
                contentDescription = "Figure",
                modifier = Modifier
                    .graphicsLayer {
                        this.shape = clip
                        this.clip = true
                    }
                    .size(sizeOfItem)
                    .clickable {
                        Log.d(TAG, "IMAGE x:%d y:%d".format(position.x, position.y))
                        setPosition(position)
                    }
                    .padding(1.dp)
            )
        }
    }

}

@Composable
fun BoardColumn (columnIndex: Int, startColor: Int, top: Dp, start: Dp, size: Int, columnCells: MutableList<Piece?>, setPosition: (Position?) -> Unit, chosenPosition: Position?) {
    val colors: ArrayList<Color> = arrayListOf(Color.DarkGray, Color.White, Color.LightGray)
    val columnSize = columnCells.size


    Column(
        modifier = Modifier
            .padding(start = start, top = top)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy((-7).dp)
    ) {
        columnCells.forEachIndexed { i, piece ->
            // column indexes are from A to L( for us it's x )
            // y for us means row
            PieceView(size.dp, colors[(i + startColor) % 3], columnCells[columnSize - 1 - i], columnIndex, columnSize - i - 1, setPosition, chosenPosition)
        }
    }
}