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
                showScoreDividers = it.showScoreDividers,
                showAddArrows = it.showAddArrows,
                showScoreRows = it.showScoreRows,
                startNumber = it.startNumber

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

    fun updateIntWithKey(key: String, num: Int) {
        viewModelScope.launch {
            settingsRepository.updateIntWithKey(key, num)
        }
    }

    fun resetSettings() {
        viewModelScope.launch {
            settingsRepository.resetSettings()
        }
    }

    fun formatNumber(num: String): String {
        val number = num.toIntOrNull()
        return number?.toString() ?: "1"
    }
}

