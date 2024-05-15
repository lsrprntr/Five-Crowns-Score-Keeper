package com.samplural.fivecrownsscorekeeper.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.samplural.fivecrownsscorekeeper.data.Players
import com.samplural.fivecrownsscorekeeper.data.scoreSeperator
import com.samplural.fivecrownsscorekeeper.ui.AppViewModelProvider


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeApp(
    modifier: Modifier = Modifier,
    viewModel: HomeAppViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(topBar = {
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
                        imageVector = Icons.Filled.Add, contentDescription = "Add Player Button"
                    )
                }
                IconButton(onClick = { viewModel.deleteAllPlayers() }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete All Button",
                    )
                }
            },

            )
    }) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            HomeBody(
                playersList = uiState.player,
                onNameChange = { id, name ->
                    viewModel.updatePlayerName(id, name)
                },
                onAddScore = { id, score ->
                    viewModel.updatePlayerScore(id, score)
                },
                checkScoreAdd = {
                    viewModel.checkScoreAdd(it)
                }
            )

        }
    }
}

@Composable
fun HomeBody(
    playersList: List<Players>,
    onNameChange: (Int, String) -> Unit,
    onAddScore: (Int, String) -> Unit,
    checkScoreAdd: (String) -> Boolean,

) {
    if (playersList.isNotEmpty()){
        LazyRow(

        ) {

            items(playersList) { player ->
                PlayerCard(
                    player = player,
                    onNameChange = onNameChange,
                    onAddScore = onAddScore,
                    checkScoreAdd = checkScoreAdd
                )
            }

        }
    } else {
        Text(text = "No Players")
    }

}


@Composable
fun PlayerCard(
    modifier: Modifier = Modifier,
    player: Players,
    onNameChange: (Int, String) -> Unit,
    onAddScore: (Int, String) -> Unit,
    checkScoreAdd: (String) -> Boolean

) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .widthIn(min = 160.dp, max = 160.dp),
    ) {
        Column(
            modifier = modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = player.id.toString())

            var textName by rememberSaveable {mutableStateOf(player.name)}

            LaunchedEffect(textName){
                //onNameChange(player.id, textName)
            }

            OutlinedTextField(
                value = textName,
                onValueChange = {
                    textName = it
                    onNameChange(player.id, textName)
                },
                label = { Text("Player Name") },
                modifier = modifier.width(IntrinsicSize.Min),
                singleLine = true,
                shape = MaterialTheme.shapes.large
                )

            val totalScore = player.scores.split(scoreSeperator).sumOf { it.toInt() }
            Text(text = "Total: $totalScore")


            Text(text = player.name)
            Column {
                val scoreList = player.scores.split(scoreSeperator)
                scoreList.forEachIndexed  { index, it ->
                    Text(text = it + "index:" + index)
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                var scoreAdd by rememberSaveable { mutableStateOf("0") }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){

                    IconButton(
                        onClick = {
                            scoreAdd = (scoreAdd.toInt() - 1).toString()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowLeft,
                            contentDescription = "Add Score Button",
                        )
                    }
                    OutlinedTextField(
                        value = scoreAdd,
                        onValueChange = {
                            if (checkScoreAdd(it)){
                                scoreAdd = it
                            }
                        },
                        label = { Text("") },
                        modifier = modifier.widthIn(max = 72.dp),
                        singleLine = true,
                        shape = MaterialTheme.shapes.extraSmall,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    IconButton(
                        onClick = {
                            scoreAdd = (scoreAdd.toInt() + 1).toString()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowRight,
                            contentDescription = "Add Score Button",
                        )
                    }
                }
                IconButton(
                    onClick = {
                        onAddScore(player.id, scoreAdd)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "Add Score to Row",
                    )
                }
            }

        }
    }
}

@Composable
fun ScoreCard(
    score: String,
    index: Int,
) {

}