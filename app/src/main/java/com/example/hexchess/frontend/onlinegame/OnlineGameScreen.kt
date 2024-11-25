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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
import com.example.hexchess.frontend.loading.LoadingScreen
import com.example.hexchess.frontend.navigation.navigateToMainMenu
import com.example.hexchess.ui.theme.LightCoral

val viewModel: OnlineGameViewModel = OnlineGameViewModel()
private const val TAG = "Online game screen"

private val whiteSideColor = arrayListOf(Color.DarkGray, Color.White, Color.LightGray)
private val blackSideColor = arrayListOf(Color.White, Color.DarkGray, Color.LightGray)
private val SIZE = 40


@Composable
fun OnlineGameScreen(navController: NavHostController = rememberNavController(), gameManager: GameManager) {
    // State to track loading status
    var isLoading by remember { mutableStateOf(true) }

    // Listen to game manager messages to handle loading state
    LaunchedEffect(key1=gameManager.isWaiting) {
        isLoading = gameManager.isWaiting
    }

    fun onCancel() {
        gameManager.disconnect()
        navController.navigateToMainMenu()
    }


    if (isLoading) {
        LoadingScreen(onCancel = { onCancel() })
    } else {
        viewModel.boardUpdate(gameManager.board.cells)
        MainGameScreen(navController, gameManager)
    }
}


@Composable
fun MainGameScreen(navController: NavHostController, gameManager: GameManager) {
    var isConnected by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = gameManager.isPlayerTurn) {
        viewModel.boardUpdate(gameManager.board.cells)
    }

    LaunchedEffect(key1 = gameManager.isConnected) {
        isConnected = gameManager.isConnected
    }

    if (viewModel.isPromotion) {
        PromotionDialog(onPieceSelected = { piece ->
            val (fx, fy) = viewModel.chosenPosition!!.getWithoutOffset()
            val (tx, ty) = viewModel.promotionTarget!!.getWithoutOffset()
            gameManager.sendPromotion(fx, fy, tx, ty, piece)
            viewModel.isPromotion = false
            viewModel.promotionTarget = null
            viewModel.availableMoves.clear()
            viewModel.chosenPosition = null
        }, onDismissRequest = {
            viewModel.isPromotion = false
            viewModel.promotionTarget = null
            viewModel.availableMoves.clear()
            viewModel.chosenPosition = null
        }, color = gameManager.color)
    }


    if (!isConnected) {
        Dialog(onDismissRequest = { navController.navigateToMainMenu() }) {
            Card(
                modifier = Modifier
                    .width(300.dp)
                    .height(150.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Opponent Disconnected",
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp
                    )
                    Button(onClick = {
                        gameManager.disconnect()
                        viewModel.availableMoves.clear()
                        isConnected = true
                        navController.navigateToMainMenu()
                    }) {
                        Text(
                            text = "OK",
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }


    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xfff0e2b9)),

    ) {
        val configuration = LocalConfiguration.current
        val radius = SIZE / 2
        val verticalPadding = radius * Math.sqrt(3.0) / 2
        val horizontalPadding = 3 * SIZE / 4
        val centerX = configuration.screenWidthDp.dp / 2 - (horizontalPadding / 2).dp - (radius / 2).dp
        val startTop = 80.dp
        val cells = remember {
            viewModel.cells
        }
        Row {
            IconButton(onClick = {
                gameManager.disconnect()
                viewModel.availableMoves.clear()
                isConnected = true
                navController.navigateToMainMenu()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Exit"
                )
            }
        }
        Column (horizontalAlignment = Alignment.Start, modifier = Modifier.padding(top = startTop - 30.dp, start = 20.dp)) {
            Text(text = "Opponent: " + gameManager.opponent, fontSize = 16.sp)
        }
        Column (horizontalAlignment = Alignment.Start, modifier = Modifier.padding(top = 450.dp, start = 20.dp)) {
            gameManager.username?.let { Text(text = "You: $it", fontSize = 16.sp) }
        }
        BoardColumn(x = 5, startColor = 0, top = startTop, start = centerX,  columnCells = cells[5], gameManager)
        for (i in 1..5) {
            val columnOffset = if (gameManager.color=="black") -i else i
            BoardColumn(
                x = 5 + columnOffset,
                startColor = (6 - i),
                top = startTop + (i * verticalPadding).dp,
                start = centerX + (i * horizontalPadding).dp,
                columnCells = cells[5 + columnOffset],
                gameManager
            )
            BoardColumn(
                x = 5 - columnOffset,
                startColor = (6 - i),
                top = startTop + (i * verticalPadding).dp,
                start = centerX - (i * horizontalPadding).dp,
                columnCells = cells[5 - columnOffset],
                gameManager
            )
        }

    }
}

@Composable
fun PieceView(
    color: Color,
    piece: Piece?,
    position: Position,
    gameManager: GameManager
) {

    val sizeOfItem = SIZE.dp
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
                if (piece?.type != null && position in viewModel.availableMoves) LightCoral else color,
                RoundedPolygonShape(polygon = hexagon)
            )
            .clip(RoundedPolygonShape(polygon = hexagon))
            .size(sizeOfItem)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                if (viewModel.chosenPosition != null && position in viewModel.availableMoves && gameManager.isPlayerTurn) {
                    val (fx, fy) = viewModel.chosenPosition!!.getWithoutOffset()
                    val (tx, ty) = position.getWithoutOffset()

                    Log.d(TAG, "FROM $fx, $fy TO $tx, $ty")

                    if (gameManager.board.cells[fx][fy]!!.type == PieceType.Pawn) {
                        if ((gameManager.board.cells[fx][fy]!!.color == PieceColor.White && ty == gameManager.board.cells[tx].size - 1)
                            || (gameManager.board.cells[fx][fy]!!.color == PieceColor.Black && ty == 0)
                        ) {
                            viewModel.promotionTarget = position
                            viewModel.isPromotion = true
                        } else {
                            gameManager.sendMove(fx, fy, tx, ty)
                            viewModel.availableMoves.clear()
                            viewModel.chosenPosition = null
                        }
                    } else {
                        gameManager.sendMove(fx, fy, tx, ty)
                        viewModel.availableMoves.clear()
                        viewModel.chosenPosition = null
                    }
                } else {
                    Log.d(TAG, "Chosen position is NULL")
                }
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
                        val playerColor =
                            if (gameManager.color == "white") PieceColor.White else PieceColor.Black
                        if (piece.color == playerColor && gameManager.isPlayerTurn) {
                            viewModel.chosenPosition = position
                            viewModel.updateAvailableMoves(gameManager.getAvailableMoves(position))
                        } else {
                            if (position in viewModel.availableMoves) {
                                val (fx, fy) = viewModel.chosenPosition!!.getWithoutOffset()
                                val (tx, ty) = position.getWithoutOffset()
                                if (gameManager.board.cells[fx][fy]!!.type == PieceType.Pawn) {
                                    if ((gameManager.board.cells[fx][fy]!!.color == PieceColor.White && ty == gameManager.board.cells[tx].size - 1)
                                        || (gameManager.board.cells[fx][fy]!!.color == PieceColor.Black && ty == 0)
                                    ) {
                                        viewModel.promotionTarget = position
                                        viewModel.isPromotion = true
                                    } else {
                                        gameManager.sendMove(fx, fy, tx, ty)
                                        viewModel.availableMoves.clear()
                                    }
                                } else {
                                    gameManager.sendMove(fx, fy, tx, ty)
                                    viewModel.availableMoves.clear()
                                }
                                viewModel.chosenPosition = null
                            }
                        }
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
    x: Int,
    startColor: Int,
    top: Dp,
    start: Dp,
    columnCells: MutableList<Piece?>,
    gameManager: GameManager
) {

    val columnSize = columnCells.size


    Column(
        modifier = Modifier
            .padding(start = start, top = top)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy((-7).dp)
    ) {
        val colors = if (gameManager.color == "white") whiteSideColor else blackSideColor
        // column indexes are from A to K( for us it's x )
        // y for us means row
        columnCells.forEachIndexed { i, _ ->
            val y = if (gameManager.color == "white") columnSize - 1 - i else i
            PieceView(colors[(i + startColor) % 3], columnCells[y], Position(x, y), gameManager)
        }
    }
}

@Composable
fun PromotionPanel() {

}