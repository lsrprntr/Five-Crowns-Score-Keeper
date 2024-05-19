package com.samplural.fivecrownsscorekeeper.data

import kotlinx.coroutines.flow.Flow

interface PlayersRepository {
    suspend fun insert(player: Players)
    fun getAllPlayers(): Flow<List<Players>>
    suspend fun deleteAllPlayers()
    suspend fun updatePlayerName(id: Int, name: String)
    suspend fun resetAllPlayerScores()
    suspend fun deletePlayerById(id: Int)
    suspend fun resetPlayerScoreById(id: Int)
    suspend fun deletePlayerScoreByIdIndex(playerId:Int, index: Int)
    fun getAllScores(): Flow<List<Scores>>
    suspend fun deleteScoreById(scoreId: Int)
    suspend fun updatePlayerScoreByIndex(scoreIndex: Int, score: String)
    suspend fun addScoreToPlayerId(scores: Scores)

}