package com.example.hexchess.backend.gamemanager

import com.google.gson.annotations.SerializedName

data class Move(
    val move_number: Int,
    val from_position: String,
    val promotion_piece: String,
    val to_position: String,
    val created_at: String
)

data class Game(
    @SerializedName("id") val id: String,
    @SerializedName("game_code") val gameCode: String,
    @SerializedName("player_white") val playerWhite: String,
    @SerializedName("player_black") val playerBlack: String,
    @SerializedName("winner") val winner: String?,
    @SerializedName("moves") val moves: List<Move>,
    @SerializedName("created_at") val createdAt: String
)