package com.samplural.fivecrownsscorekeeper.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class Players(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String = "",
    val scores: String = ""
)

const val scoreSeperator = ","