package com.samplural.fivecrownsscorekeeper.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayersDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(player: Players)

    @Query("SELECT * from players ORDER BY id ASC")
    fun getAllPlayers(): Flow<List<Players>>

    @Query("DELETE FROM players")
    suspend fun deleteAllPlayers()

    @Query("UPDATE players SET name = :name WHERE id = :id")
    suspend fun updatePlayerName(id: Int, name: String)

    @Query("DELETE FROM scores")
    suspend fun resetAllPlayerScores()
    @Query("DELETE FROM players WHERE id = :id")
    suspend fun deletePlayerById(id: Int)

    @Query("DELETE FROM scores WHERE playerid = :playerId")
    suspend fun resetPlayerScoresById(playerId: Int)

    @Query("SELECT scores FROM scores WHERE playerId = :playerId")
    fun getAllPlayerScoresById(playerId: Int): Flow<List<String>>
    @Query("SELECT * from scores ORDER BY scoreId ASC")

    fun getAllScores(): Flow<List<Scores>>
    @Query("DELETE FROM scores WHERE scoreId = :scoreId")

    suspend fun deleteScoreById(scoreId: Int)
    @Query("UPDATE scores SET scores = :score WHERE scoreId = :scoreIndex")
    suspend fun updatePlayerScoreByIndex(scoreIndex: Int, score: String)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addScoreToPlayerId(scores: Scores)

}