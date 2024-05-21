package com.samplural.fivecrownsscorekeeper.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samplural.fivecrownsscorekeeper.data.Players
import com.samplural.fivecrownsscorekeeper.data.PlayersRepository
import com.samplural.fivecrownsscorekeeper.data.Scores
import com.samplural.fivecrownsscorekeeper.data.SettingsRepository
import com.samplural.fivecrownsscorekeeper.data.UserPreferences
import com.samplural.fivecrownsscorekeeper.ui.screens.PlayerCardUiState.Companion.TIMEOUT_MILLIS
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeAppViewModel(
    private val playersRepository: PlayersRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {

    val uiState: StateFlow<PlayerCardUiState> =
        playersRepository.getAllPlayers().map { PlayerCardUiState(it) }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = PlayerCardUiState()
        )

    val scoreUiState: StateFlow<ScoresUiState> =
        playersRepository.getAllScores().map{ ScoresUiState(it) }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ScoresUiState()
        )

    val settingsUiState: StateFlow<UserPreferences> =
        settingsRepository.userPreferencesFlow.map {
            UserPreferences(
                showIncrementArrows = it.showIncrementArrows,
                showDeleteRows = it.showDeleteRows,
                showRoundLabels = it.showRoundLabels,
                showScoreDividers = it.showScoreDividers,
                showEditNumbers = it.showEditNumbers,
                showAddArrows = it.showAddArrows,
                showScoreRows = it.showScoreRows,

            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = UserPreferences()
        )


    fun addNewPlayer() {
        viewModelScope.launch {
            playersRepository.insert(Players(id = 0, name = ""))
        }
    }
    fun addScoreToPlayer(playerId: Int, score: String) {
        viewModelScope.launch {
            val scores = Scores(scoreId = 0, playerId = playerId, scores = score)
            playersRepository.addScoreToPlayerId(scores)
        }
    }

    fun updatePlayerName(id: Int, name: String) {
        viewModelScope.launch {
            playersRepository.updatePlayerName(id, name)

        }
    }

    fun updatePlayerScoreByIndex(scoreIndex: Int, score: String) {
        if (checkScoreAdd(score)) {
            viewModelScope.launch {
                playersRepository.updatePlayerScoreByIndex(scoreIndex, score)
            }
        }
    }

    fun resetAllPlayerScores() {
        viewModelScope.launch {
            playersRepository.resetAllPlayerScores()
        }
    }

    fun deleteAllPlayers() {
        viewModelScope.launch {
            playersRepository.deleteAllPlayers()
        }
    }


    fun deletePlayerById(id: Int) {
        viewModelScope.launch {
            playersRepository.deletePlayerById(id)
        }
    }
    fun resetPlayerScoreById(id: Int) {
        viewModelScope.launch {
            playersRepository.resetPlayerScoreById(id)
        }
    }
    fun deletePlayerScoreById(scoreId: Int) {
        viewModelScope.launch {
            playersRepository.deleteScoreById(scoreId)

        }
    }

    // Should return true if number input is valid
    fun checkScoreAdd(score: String): Boolean {
        if (score == "") {
            return false
        }
        return score.toIntOrNull() != null
    }

    // Should return string if number input is invalid
    fun formatScoreAdd(score: String): String {
        // Captures negative dash and numbers after it trimming zeros
        val regex = Regex("(-?)0*(\\d*\$)")
        val match = regex.find(score)
        if (match != null){
            return match.groupValues.takeLast(2).joinToString("")
        }
        return "0"
    }
}


data class PlayerCardUiState(
    val player: List<Players> = emptyList(),
) {
    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }
}

data class ScoresUiState(
    val scores: List<Scores> = emptyList()
){
}