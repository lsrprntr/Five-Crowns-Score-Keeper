package com.samplural.fivecrownsscorekeeper.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayersDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(player: Players)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(player: Players)

    @Delete
    suspend fun delete(player: Players)

    @Query("SELECT * from players ORDER BY id ASC")
    fun getAllPlayers(): Flow<List<Players>>

    @Query("DELETE FROM players")
    suspend fun deleteAllPlayers()

    @Query("UPDATE players SET name = :name WHERE id = :id")
    suspend fun updatePlayerName(id: Int, name: String)


}