package com.samplural.fivecrownsscorekeeper.data

import kotlinx.coroutines.flow.Flow

interface PlayersRepository {
    suspend fun insert(player: Players)
    suspend fun update(player: Players)
    suspend fun delete(player: Players)
    fun getAllPlayers(): Flow<List<Players>>
    suspend fun deleteAllPlayers()
    suspend fun updatePlayerName(id: Int, name: String)
    suspend fun updatePlayerScore(id: Int, score: String)
    suspend fun getPlayerScores(id: Int): String
    suspend fun resetAllPlayerScores()
    suspend fun deletePlayerById(id: Int)

}