package com.samplural.fivecrownsscorekeeper.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class Players(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String = "",
    val scores: String = "0,"
)
