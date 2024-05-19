package com.samplural.fivecrownsscorekeeper.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samplural.fivecrownsscorekeeper.data.Players
import com.samplural.fivecrownsscorekeeper.data.PlayersRepository
import com.samplural.fivecrownsscorekeeper.data.Scores
import com.samplural.fivecrownsscorekeeper.data.scoreSeperator
import com.samplural.fivecrownsscorekeeper.ui.screens.PlayerCardUiState.Companion.TIMEOUT_MILLIS
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeAppViewModel(
    private val playersRepository: PlayersRepository,
) : ViewModel() {

    /*
    private val _uiState = MutableStateFlow(PlayerCardUiState())
    val uiState: StateFlow<PlayerCardUiState> = _uiState

    init {
        viewModelScope.launch {
            playersRepository.getAllPlayers().collect { it ->
                _uiState.value = PlayerCardUiState(it)
            }
        }
    }
    */

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
            playersRepository.insert(Players(id = 0, name = "", scores = ""))
        }
    }

    fun updatePlayerName(id: Int, name: String) {
        viewModelScope.launch {
            playersRepository.updatePlayerName(id, name)

        }
    }

    fun updatePlayerScore(id: Int, score: String) {
        if (checkScoreAdd(score)) {
            viewModelScope.launch {
                // Current Scores and check if first score
                val scores = playersRepository.getPlayerScores(id)
                if (scores != "") {
                    val updatedScores = scores + scoreSeperator + score
                    playersRepository.updatePlayerScore(id, updatedScores)
                } else {
                    playersRepository.updatePlayerScore(id, score)
                }
            }
        }
    }

    fun updatePlayerScoreByIndex(id: Int, index: Int, score: String) {
        if (checkScoreAdd(score)) {
            viewModelScope.launch {
                val scoresList =
                    playersRepository.getPlayerScores(id).split(scoreSeperator).toMutableList()
                scoresList[index] = score
                playersRepository.updatePlayerScore(id, scoresList.joinToString(scoreSeperator))
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


    fun deletePlayerScoreById(playerId: Int, index: Int) {
        viewModelScope.launch {
            val player = playersRepository.getPlayerById(playerId)
            val scores = player.scores
            val updatedScores = scores.split(scoreSeperator).toMutableList()
            updatedScores.removeAt(index)
            val newPlayerObject = Players(player.id, player.name, updatedScores.joinToString(scoreSeperator))
            playersRepository.deletePlayerById(playerId)
            playersRepository.insert(newPlayerObject)

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
    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }
}