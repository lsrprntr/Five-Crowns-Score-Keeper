package com.samplural.fivecrownsscorekeeper.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samplural.fivecrownsscorekeeper.data.Players
import com.samplural.fivecrownsscorekeeper.data.PlayersRepository
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

    val playerList = uiState.value.player

    fun addTestPlayer() {
        viewModelScope.launch {
            playersRepository.insert(Players(id = 0, name = "test", scores = "1,1,1,1"))
        }
    }
    fun addPlayer(){
        viewModelScope.launch {
            playersRepository.insert(Players(id = 0))
        }
    }
    fun updatePlayerName(id: Int, name: String) {
        viewModelScope.launch {
            playersRepository.updatePlayerName(id, name)

        }
    }

    fun updatePlayerScore(id: Int, score: String) {
        viewModelScope.launch {
            playersRepository.updatePlayerScore(id, score)
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