package com.samplural.fivecrownsscorekeeper.ui.screens

import androidx.lifecycle.ViewModel
import com.samplural.fivecrownsscorekeeper.data.PlayersRepository
import com.samplural.fivecrownsscorekeeper.data.SettingsRepository

class RankingScreenViewModel(
    private val playersRepository: PlayersRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

}
