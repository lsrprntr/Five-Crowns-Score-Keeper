package com.samplural.fivecrownsscorekeeper.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samplural.fivecrownsscorekeeper.data.Players
import com.samplural.fivecrownsscorekeeper.data.PlayersRepository
import com.samplural.fivecrownsscorekeeper.data.Scores
import com.samplural.fivecrownsscorekeeper.ui.screens.PlayerCardUiState.Companion.TIMEOUT_MILLIS
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeAppViewModel(
    private val playersRepository: PlayersRepository,
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

    fun addNewPlayer() {
        viewModelScope.launch {
            playersRepository.insert(Players(id = 0, name = ""))
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

    // Should return true if number input is valid
    fun checkScoreAdd(score: String): Boolean {
        return score.toIntOrNull() != null
    }

    // Should return string if number input is invalid
    fun formatScoreAdd(score: String): String {
        if (score.toIntOrNull() == null) {
            return score.trimStart('0')
        }
        return score.trimStart('0').toInt().toString()
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

    fun addScoreToPlayer(playerId: Int, score: String) {
        viewModelScope.launch {
            val scores = Scores(scoreId = 0, playerId = playerId, scores = score)
            playersRepository.addScoreToPlayerId(scores)
        }
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