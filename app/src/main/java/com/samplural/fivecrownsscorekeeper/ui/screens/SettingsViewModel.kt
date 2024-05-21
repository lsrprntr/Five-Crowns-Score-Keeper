package com.samplural.fivecrownsscorekeeper.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samplural.fivecrownsscorekeeper.data.SettingsRepository
import com.samplural.fivecrownsscorekeeper.data.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val uiState: StateFlow<UserPreferences> =
        settingsRepository.userPreferencesFlow.map {
            UserPreferences(
                showIncrementArrows = it.showIncrementArrows,
                showDeleteRows = it.showDeleteRows,
                showRoundLabels = it.showRoundLabels,
                showExpandedScores = it.showExpandedScores,
                showEditNumbers = it.showEditNumbers,
                showScoreDividers = it.showScoreDividers
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(PlayerCardUiState.TIMEOUT_MILLIS),
            initialValue = UserPreferences()
        )

    fun updateBooleanWithKey(key: String, bool: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateBooleanWithKey(key, bool)
        }
    }
}

