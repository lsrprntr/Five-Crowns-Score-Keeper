package com.samplural.fivecrownsscorekeeper.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.samplural.fivecrownsscorekeeper.PlayersApplication
import com.samplural.fivecrownsscorekeeper.ui.screens.HomeAppViewModel
import com.samplural.fivecrownsscorekeeper.ui.screens.RankingScreenViewModel
import com.samplural.fivecrownsscorekeeper.ui.screens.SettingsViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeAppViewModel(
                playersApplication().container.playersRepository,
                playersApplication().container.settingsRepository
            )
        }
        initializer {
            SettingsViewModel(
                playersApplication().container.settingsRepository
            )
        }
        initializer {
            RankingScreenViewModel(
                playersApplication().container.playersRepository,
                playersApplication().container.settingsRepository
            )
        }


    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [PlayersApplication].
 */
fun CreationExtras.playersApplication(): PlayersApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PlayersApplication)