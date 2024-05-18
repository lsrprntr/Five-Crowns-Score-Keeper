package com.samplural.fivecrownsscorekeeper.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samplural.fivecrownsscorekeeper.data.Players
import com.samplural.fivecrownsscorekeeper.data.PlayersRepository
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

    val uiState: StateFlow<PlayerCardUiState> =
        playersRepository.getAllPlayers().map { PlayerCardUiState(it) }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = PlayerCardUiState()
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

    fun updatePlayerScoreByIndex(id: Int, index: Int, score: String){
        if (checkScoreAdd(score)) {
            viewModelScope.launch {
                val scoresList =
                    playersRepository.getPlayerScores(id).split(scoreSeperator).toMutableList()
                scoresList[index] = score
                playersRepository.updatePlayerScore(id, scoresList.joinToString(scoreSeperator))
            }
        }
    }

    fun resetAllPlayerScores(){
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
            return score
        }
        return score.toInt().toString()
    }

    fun deletePlayerById(id: Int) {
        viewModelScope.launch {
            playersRepository.deletePlayerById(id)
        }
    }


}

data class PlayerCardUiState(
    val player: List<Players> = emptyList()
) {
    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }
}