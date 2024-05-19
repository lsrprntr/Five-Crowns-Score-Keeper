package com.samplural.fivecrownsscorekeeper.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Players::class, Scores::class], version = 2, exportSchema = false)
abstract class PlayersDatabase : RoomDatabase() {

    abstract fun playersDao(): PlayersDao

    // Provides the Instance of the Database to the application
    companion object {
        @Volatile
        private var Instance: PlayersDatabase? = null

        fun getDatabase(context: Context): PlayersDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, PlayersDatabase::class.java, "player_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}