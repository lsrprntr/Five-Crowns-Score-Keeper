package com.samplural.fivecrownsscorekeeper.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class Players(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String = "",
    val scores: String = "",
)

@Entity(
    tableName = "scores",
    foreignKeys = [ForeignKey(entity = Players::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("playerId"),
    onDelete = ForeignKey.CASCADE)]
)
data class Scores(
    @PrimaryKey(autoGenerate = true)
    val scoreId: Int,
    val playerId: Int,
    val scores: String = ""
)

const val scoreSeperator = ","