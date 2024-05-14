package com.samplural.fivecrownsscorekeeper.data;

import kotlinx.coroutines.flow.Flow

class LocalPlayersRepository(private val playersDao: PlayersDao): PlayersRepository {
    override suspend fun insert(player: Players)  = playersDao.insert(player)
    override suspend fun update(player: Players) = playersDao.update(player)
    override suspend fun delete(player: Players) = playersDao.delete(player)
    override fun getAllPlayers(): Flow<List<Players>> = playersDao.getAllPlayers()
    override suspend fun deleteAllPlayers() = playersDao.deleteAllPlayers()
}
