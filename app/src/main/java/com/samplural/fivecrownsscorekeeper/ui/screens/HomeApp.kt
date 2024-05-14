package com.samplural.fivecrownsscorekeeper.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.samplural.fivecrownsscorekeeper.data.Players
import com.samplural.fivecrownsscorekeeper.ui.AppViewModelProvider


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeApp(
    modifier: Modifier = Modifier,
    viewModel: HomeAppViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Score Keeper")
                },
                actions = {
                    IconButton(onClick = { viewModel.addTestPlayer() }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add Player Button"
                        )
                    }
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete All Button",
                            )
                    }
                },

            )
        }
    ){ paddingValues ->
        Column(modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
        ) {

            HomeBody(playersList = uiState.player)

        }
    }
}

@Composable
fun HomeBody(
    playersList: List<Players>
){
    LazyRow(

    ) {
        items(playersList){player ->
            PlayerCard(player)
        }
    }
}

@Composable
fun PlayerCard(
    player: Players
) {
    Card(){
        Column() {
            Text(text = player.id.toString())

            Text(text = player.name)
            Text(text = player.scores)
        }
    }
}

@Composable
fun ScoreCard(){

}