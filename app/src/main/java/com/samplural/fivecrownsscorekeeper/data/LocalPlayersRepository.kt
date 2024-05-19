package com.samplural.fivecrownsscorekeeper.data;

import kotlinx.coroutines.flow.Flow

class LocalPlayersRepository(private val playersDao: PlayersDao): PlayersRepository {
    override suspend fun insert(player: Players)  = playersDao.insert(player)
    override fun getAllPlayers(): Flow<List<Players>> = playersDao.getAllPlayers()
    override suspend fun deleteAllPlayers() = playersDao.deleteAllPlayers()
    override suspend fun updatePlayerName(id: Int, name: String) = playersDao.updatePlayerName(id, name)
    override suspend fun resetAllPlayerScores() = playersDao.resetAllPlayerScores()
    override suspend fun deletePlayerById(id: Int) = playersDao.deletePlayerById(id)
    override suspend fun resetPlayerScoreById(id: Int) = playersDao.resetPlayerScoresById(id)
    override suspend fun deletePlayerScoreByIdIndex(playerId: Int, index: Int) {}
    override fun getAllScores(): Flow<List<Scores>> = playersDao.getAllScores()
    override suspend fun deleteScoreById(scoreId: Int) = playersDao.deleteScoreById(scoreId)
    override suspend fun updatePlayerScoreByIndex(scoreIndex: Int, score: String) = playersDao.updatePlayerScoreByIndex(scoreIndex, score)
    override suspend fun addScoreToPlayerId(scores: Scores) = playersDao.addScoreToPlayerId(scores)
}
