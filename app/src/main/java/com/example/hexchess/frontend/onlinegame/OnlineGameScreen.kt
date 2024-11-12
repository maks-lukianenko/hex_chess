package com.example.hexchess.frontend.onlinegame

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
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
import com.example.hexchess.backend.gamemanager.GameManager
import com.example.hexchess.utills.RoundedPolygonShape
import com.example.hexchess.backend.onlinegame.Piece
import com.example.hexchess.backend.onlinegame.PieceColor
import com.example.hexchess.backend.onlinegame.PieceType
import com.example.hexchess.backend.onlinegame.Position
import com.example.hexchess.frontend.navigation.navigateToMainMenu
import com.example.hexchess.ui.theme.LightCoral

val viewModel: OnlineGameViewModel = OnlineGameViewModel()
private const val TAG = "Online game screen"

private val whiteSideColor: ArrayList<Color> = arrayListOf(Color.DarkGray, Color.White, Color.LightGray)
private val blackSideColor: ArrayList<Color> = arrayListOf(Color.White, Color.DarkGray, Color.LightGray)


@Composable
fun OnlineGameScreen(navController: NavHostController = rememberNavController(), gameManager: GameManager) {

    LaunchedEffect(Unit) {
        gameManager.connectToGame("a116c6")
    }

    var chosenPosition by remember {
        mutableStateOf<Position?>(null)
    }

    val setPosition: (Position?) -> Unit = { position ->
        chosenPosition = position
    }

    viewModel.boardUpdate(gameManager.getBoardState())
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xfff0e2b9)),

    ) {
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
        Row {
            IconButton(onClick = {
                gameManager.disconnect()
                navController.navigateToMainMenu()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Exit"
                )
            }
        }
        BoardColumn(columnIndex = 5, startColor = 0, top = startTop, start = centerX, size = size, columnCells = cells[5], setPosition, chosenPosition, gameManager)
        for (i in 1..5) {
            BoardColumn(
                columnIndex = 5 + i,
                startColor = (6 - i),
                top = startTop + (i * verticalPadding).dp,
                start = centerX + (i * horizontalPadding).dp,
                size = size, columnCells = cells[5 + i],
                setPosition,
                chosenPosition,
                gameManager
            )
            BoardColumn(
                columnIndex = 5 - i,
                startColor = (6 - i),
                top = startTop + (i * verticalPadding).dp,
                start = centerX - (i * horizontalPadding).dp,
                size = size,
                columnCells = cells[5 - i],
                setPosition,
                chosenPosition,
                gameManager
            )
        }
    }
}

@Composable
fun PieceView(
    sizeOfItem: Dp,
    color: Color,
    piece: Piece?,
    x: Int, y: Int,
    setPosition: (Position?) -> Unit,
    chosenPosition: Position?,
    gameManager: GameManager
) {
    // column indexes are from A to L( for us it's x )
    // y for us means row

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

    val position = Position(x, y)

    Box (
        modifier = Modifier
            .border(1.dp, Color.Black, RoundedPolygonShape(polygon = hexagon))
            .background(
                if (piece?.type != null && position in viewModel.availableMoves) LightCoral else color,
                RoundedPolygonShape(polygon = hexagon)
            )
            .clip(RoundedPolygonShape(polygon = hexagon))
            .size(sizeOfItem)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                Log.d(TAG, "BOX x:%d y:%d".format(position.x, position.y))
                if (chosenPosition != null && position in viewModel.availableMoves) {
                    val (fx, fy) = chosenPosition.getWithoutOffset()
                    gameManager.sendMove(fx, fy, x, y, "test") // TODO put username as parameter
                    viewModel.boardUpdate(gameManager.getBoardState())
                    viewModel.availableMoves.clear()
                    Log.d(
                        TAG,
                        "Chosen position x:%d y:%d".format(chosenPosition.x, chosenPosition.y)
                    )
                } else {
                    Log.d(TAG, "Chosen position is NULL")
                }
                setPosition(null)
            }
    ) {
        if (piece != null) {
            Image(
                painter = painterResource(
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
                        viewModel.updateAvailableMoves(gameManager.getAvailableMoves(position))
                    }
            )
        } else if (position in viewModel.availableMoves) {
            Canvas(
                modifier = Modifier
                    .size(sizeOfItem)
                    .align(Alignment.Center)
                    .alpha(0.7f)
            ) {
                drawCircle(
                    color = Color.Gray,
                    radius = 10.dp.toPx() / 2f,
                    center = Offset(sizeOfItem.toPx() / 2, sizeOfItem.toPx() / 2)
                )
            }
        }
    }

}

@Composable
fun BoardColumn (
    columnIndex: Int,
    startColor: Int,
    top: Dp,
    start: Dp,
    size: Int,
    columnCells: MutableList<Piece?>,
    setPosition: (Position?) -> Unit,
    chosenPosition: Position?,
    gameManager: GameManager
) {

    val columnSize = columnCells.size


    Column(
        modifier = Modifier
            .padding(start = start, top = top)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy((-7).dp)
    ) {
        if (viewModel.color == "white") {
            val colors: ArrayList<Color> = arrayListOf(Color.DarkGray, Color.White, Color.LightGray)
            columnCells.forEachIndexed { i, _ ->
                // column indexes are from A to L( for us it's x )
                // y for us means row
                PieceView(size.dp, colors[(i + startColor) % 3], columnCells[columnSize - 1 - i], columnIndex, columnSize - i - 1, setPosition, chosenPosition, gameManager)
            }

        } else {
            val colors: ArrayList<Color> = arrayListOf(Color.White, Color.DarkGray, Color.LightGray)
            columnCells.forEachIndexed { i, _ ->
                // column indexes are from A to L( for us it's x )
                // y for us means row
                PieceView(size.dp, colors[(i + startColor) % 3], columnCells[i], columnIndex, i, setPosition, chosenPosition, gameManager)
            }
        }
    }
}