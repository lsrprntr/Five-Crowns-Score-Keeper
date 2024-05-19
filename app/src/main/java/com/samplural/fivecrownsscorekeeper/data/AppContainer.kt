package com.samplural.fivecrownsscorekeeper.data

import android.content.Context

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
        SettingsRepository(context.dataStore)
    }

}

