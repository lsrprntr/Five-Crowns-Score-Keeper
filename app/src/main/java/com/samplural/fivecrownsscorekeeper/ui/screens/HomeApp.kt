package com.samplural.fivecrownsscorekeeper.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
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
    var dropDownMenuExpanded by remember { mutableStateOf(false) }


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
                    IconButton(onClick = { viewModel.addNewPlayer() }) {
                        Icon(
                            imageVector = Icons.Filled.Add, contentDescription = "Add Player Button"
                        )
                    }
                    IconButton(onClick = { dropDownMenuExpanded = !dropDownMenuExpanded }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "More Options",
                        )
                    }
                    DropdownMenu(
                        expanded = dropDownMenuExpanded,
                        onDismissRequest = { dropDownMenuExpanded = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            viewModel.resetAllPlayerScores()
                            dropDownMenuExpanded = false
                        },
                            text = { Text("Reset All Scores") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Refresh,
                                    contentDescription = "Delete All Button",
                                )
                            }
                        )
                        DropdownMenuItem(onClick = {
                            viewModel.deleteAllPlayers()
                            dropDownMenuExpanded = false
                        },
                            text = { Text("Delete All Players") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = "Delete All Button",
                                )
                            }
                        )
                        DropdownMenuItem(onClick = { /* TODO:AddAboutMe */ },
                            text = { Text("About") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Info,
                                    contentDescription = "Delete All Button",
                                )
                            }
                        )
                    }

                },
            )
        }
    ) { paddingValues ->
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
                },
                onChangeScore = { id, index, score ->
                    viewModel.updatePlayerScoreByIndex(id, index, score)
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
    onChangeScore: (Int, Int, String) -> Unit,
    modifier: Modifier = Modifier

) {
    Log.d("DEBUG", "playersList: $playersList")
    if (playersList.isNotEmpty()) {
        LazyRow(
            modifier = modifier.padding(start = 4.dp, end = 4.dp, bottom = 4.dp)
        ) {

            items(playersList) { player ->
                PlayerCard(
                    player = player,
                    onNameChange = onNameChange,
                    onAddScore = onAddScore,
                    checkScoreAdd = checkScoreAdd,
                    onChangeScore = onChangeScore,
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
    checkScoreAdd: (String) -> Boolean,
    onChangeScore: (Int, Int, String) -> Unit
) {
    Card(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .widthIn(min = 112.dp, max = 160.dp),
    ) {
        // Player Details And Score Card
        Column(
            modifier = modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .weight(1f)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {

            var textName by rememberSaveable { mutableStateOf(player.name) }

            OutlinedTextField(
                value = textName,
                onValueChange = {
                    textName = it
                    onNameChange(player.id, textName)
                },
                placeholder = {
                    Text(
                        text = "Player Name",
                        style = MaterialTheme.typography.bodySmall,
                    )
                },
                modifier = modifier.width(intrinsicSize = IntrinsicSize.Max),
                singleLine = true,
                shape = MaterialTheme.shapes.large,
            )

            val playerScores = player.scores

            val validScoreCard = playerScores.isNotEmpty() || (playerScores == "0")

            val totalScore = if (validScoreCard) {
                playerScores.split(scoreSeperator).sumOf { it.toInt() }
            } else {
                0
            }

            Text(
                text = "Total: $totalScore",
                modifier = modifier.padding(vertical = 8.dp),
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outline)

            // Score Card Layout
            if (validScoreCard) {
                LazyColumn(
                    modifier = modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                ) {
                    itemsIndexed(playerScores.split(scoreSeperator)) { index, it ->
                        ScoreLine(
                            playerId = player.id,
                            index = index,
                            score = it,
                            checkScoreAdd = checkScoreAdd,
                            onChangeScore = onChangeScore,
                            modifier = modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier.fillMaxSize()
                ) {
                    Text(
                        text = "No Scores Added",
                        textAlign = TextAlign.Center
                    )
                }
            }

        }

        HorizontalDivider(
            modifier = modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.outline
        )

        // Score Addition Buttons
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier.padding(top = 8.dp)
        ) {
            var scoreAdd by rememberSaveable { mutableStateOf("0") }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = {
                        scoreAdd = (scoreAdd.toInt() - 1).toString()
                    },
                    modifier = modifier.weight(9f),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Add Score Button",
                    )
                }
                OutlinedTextField(
                    value = scoreAdd,
                    onValueChange = {
                        if (checkScoreAdd(it)) {
                            scoreAdd = it
                        }
                    },
                    label = { Text("") },
                    modifier = modifier
                        .widthIn(max = 72.dp)
                        .weight(10f),
                    singleLine = true,
                    shape = MaterialTheme.shapes.extraSmall,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                )
                IconButton(
                    onClick = {
                        scoreAdd = (scoreAdd.toInt() + 1).toString()
                    },
                    modifier = modifier.weight(9f),

                    ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Add Score Button",
                    )
                }
            }
            IconButton(onClick = {
                onAddScore(player.id, scoreAdd)
            }
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Add Score to Row",
                    modifier = modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun ScoreLine(
    modifier: Modifier = Modifier,
    playerId: Int,
    index: Int,
    score: String,
    checkScoreAdd: (String) -> Boolean,
    onChangeScore: (Int, Int, String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        var currentScore by rememberSaveable { mutableStateOf(score) }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = {
                    currentScore = (currentScore.toInt() - 1).toString()
                    onChangeScore(playerId, index, currentScore)

                },
                modifier = modifier.weight(9f),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Add Score Button",
                )
            }
            OutlinedTextField(
                value = currentScore,
                onValueChange = {
                    if (checkScoreAdd(it)) {
                        currentScore = it
                        onChangeScore(playerId, index, it)
                    }
                },
                label = { Text("") },
                modifier = modifier
                    .widthIn(max = 72.dp)
                    .weight(10f),
                singleLine = true,
                shape = MaterialTheme.shapes.extraSmall,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(textAlign = TextAlign.Center),
            )
            IconButton(
                onClick = {
                    currentScore = (currentScore.toInt() + 1).toString()
                    onChangeScore(playerId, index, currentScore)

                },
                modifier = modifier.weight(9f),

                ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Add Score Button",
                )
            }
        }
    }
}