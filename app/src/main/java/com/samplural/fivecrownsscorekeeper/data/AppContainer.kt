package com.samplural.fivecrownsscorekeeper.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

interface AppContainer {
    val playersRepository: PlayersRepository
    val settingsRepository: SettingsRepository
}

// DI class taking context as dependency
class AppDataContainer(
    private val context: Context
) : AppContainer {
    override val playersRepository: PlayersRepository by lazy {
        LocalPlayersRepository(PlayersDatabase.getDatabase(context).playersDao())
    }
    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepository(dataStore)
    }

    // Provide a getter for the dataStore
    private val dataStore: DataStore<Preferences>
        get() = context.dataStore

}

