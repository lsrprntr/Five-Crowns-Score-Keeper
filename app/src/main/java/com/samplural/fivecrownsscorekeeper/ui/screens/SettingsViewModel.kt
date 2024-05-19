package com.samplural.fivecrownsscorekeeper.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samplural.fivecrownsscorekeeper.data.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = settingsRepository.showDetails
        .map { isLinearLayout ->
            SettingsUiState(isLinearLayout)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState()
        )

    fun switchShowDetails(bool: Boolean) {
        viewModelScope.launch {
            settingsRepository.switchShowDetails(bool)
        }
    }

    fun switchShowArrows(bool: Boolean) {
        viewModelScope.launch {
            settingsRepository.switchShowArrows(bool)
        }
    }

}

data class SettingsUiState(
    val showDetails: Boolean = false,
    val showArrows: Boolean = false,
)
