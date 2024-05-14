package com.samplural.fivecrownsscorekeeper.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface PlayersRepository {
    suspend fun insert(player: Players)
    suspend fun update(player: Players)
    suspend fun delete(player: Players)
    fun getAllPlayers(): Flow<List<Players>>
    suspend fun deleteAllPlayers()

}