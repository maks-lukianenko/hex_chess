package com.example.hexchess.backend.gamemanager

import com.google.gson.annotations.SerializedName

data class Move (
    val from: String,
    val to: String,
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