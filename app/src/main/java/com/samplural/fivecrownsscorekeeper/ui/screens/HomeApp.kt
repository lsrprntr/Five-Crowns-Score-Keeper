package com.samplural.fivecrownsscorekeeper.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.samplural.fivecrownsscorekeeper.R
import com.samplural.fivecrownsscorekeeper.data.Players
import com.samplural.fivecrownsscorekeeper.data.Scores
import com.samplural.fivecrownsscorekeeper.data.scoreSeperator
import com.samplural.fivecrownsscorekeeper.ui.AppViewModelProvider
import com.samplural.fivecrownsscorekeeper.ui.templates.CompactOutlinedTextField


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeApp(
    modifier: Modifier = Modifier,
    viewModel: HomeAppViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onSettingsClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scoreUiState by viewModel.scoreUiState.collectAsState()

    var dropDownMenuExpanded by remember { mutableStateOf(false) }

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
                DropdownMenu(expanded = dropDownMenuExpanded,
                    onDismissRequest = { dropDownMenuExpanded = false }) {
                    DropdownMenuItem(onClick = {
                        viewModel.resetAllPlayerScores()
                        dropDownMenuExpanded = false
                    }, text = { Text("Reset All Scores") }, leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Reset All Scores Button",
                        )
                    })
                    DropdownMenuItem(onClick = {
                        viewModel.deleteAllPlayers()
                        dropDownMenuExpanded = false
                    }, text = { Text("Delete All Players") }, leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Delete All Players Button",
                        )
                    })
                    DropdownMenuItem(onClick = {
                        onSettingsClick()
                        dropDownMenuExpanded = false
                    }, text = { Text("Settings") }, leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings Button",
                        )
                    })
                    DropdownMenuItem(onClick = { /* TODO:AddAboutMe */ },
                        text = { Text("About") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "About Me Button",
                            )
                        })
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
                scoresList = scoreUiState.scores,
                modifier = modifier.padding(top = 16.dp),
                onNameChange = { id, name ->
                    viewModel.updatePlayerName(id, name)
                },
                onAddScore = { playerId, score ->
                    viewModel.addScoreToPlayer(playerId, score)
                },
                checkScoreAdd = { viewModel.checkScoreAdd(it) },
                onChangeScore = { scoreIndex, score ->
                    viewModel.updatePlayerScoreByIndex(scoreIndex, score)
                },
                onDeletePlayer = { viewModel.deletePlayerById(it) },
                formatScoreAdd = { viewModel.formatScoreAdd(it) },
                onDeleteScore = { viewModel.deletePlayerScoreById(it) },
                onResetPlayerScore = { viewModel.resetPlayerScoreById(it) },
            )
        }
    }
}

@Composable
fun HomeBody(
    playersList: List<Players>,
    scoresList: List<Scores>,
    onNameChange: (Int, String) -> Unit,
    onAddScore: (Int, String) -> Unit,
    checkScoreAdd: (String) -> Boolean,
    onChangeScore: (Int, String) -> Unit,
    onDeletePlayer: (Int) -> Unit,
    formatScoreAdd: (String) -> String,
    onDeleteScore: (Int) -> Unit,
    onResetPlayerScore: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (playersList.isNotEmpty()) {
        LazyRow(
            modifier = modifier.padding(start = 4.dp, end = 4.dp, bottom = 4.dp)
        ) {

            items(playersList, key = { it.id }) { player ->
                Box(
                    contentAlignment = Alignment.TopEnd
                ) {
                    val filteredScores = scoresList.filter { it.playerId == player.id }
                    PlayerCard(
                        player = player,
                        scores = filteredScores,
                        onNameChange = onNameChange,
                        onAddScore = onAddScore,
                        checkScoreAdd = checkScoreAdd,
                        onChangeScore = onChangeScore,
                        formatScoreAdd = formatScoreAdd,
                        onDeleteScore = onDeleteScore,
                        onResetPlayerScore = onResetPlayerScore,
                    )
                    IconButton(
                        onClick = { onDeletePlayer(player.id) },
                        modifier = modifier
                            .size(16.dp)
                            .offset(y = (-20).dp),
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.remove_circle),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                        )
                    }
                }
            }

        }
    } else {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            val state = remember {
                MutableTransitionState(false).apply {
                    // Start the animation immediately.
                    targetState = true
                }
            }
            AnimatedVisibility(
                visibleState = state,
                enter = fadeIn(),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Press '+' to add a player")
                }
            }
        }
    }
}


@Composable
fun PlayerCard(
    modifier: Modifier = Modifier,
    player: Players,
    onNameChange: (Int, String) -> Unit,
    onAddScore: (Int, String) -> Unit,
    checkScoreAdd: (String) -> Boolean,
    onChangeScore: (Int, String) -> Unit,
    formatScoreAdd: (String) -> String,
    onDeleteScore: (Int) -> Unit,
    onResetPlayerScore: (Int) -> Unit,
    scores: List<Scores>,
) {

    val justScores = scores.map { it.scores }
    val playerScores = justScores.joinToString(scoreSeperator)

    val validScoreCard = playerScores.isNotEmpty() || (playerScores == "0")
    val totalScore = if (validScoreCard) {
        playerScores.split(scoreSeperator).sumOf { it.trimStart(' ').toInt() }
    } else {
        0
    }

    Card(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .widthIn(min = 160.dp, max = 160.dp),
    ) {

        // Player Details And Score Card
        Column(
            modifier = modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {

            var textName by rememberSaveable { mutableStateOf(player.name) }

            // Player Name Box
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.fillMaxWidth()
            ) {
                CompactOutlinedTextField(
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
                    modifier = modifier.heightIn(min = 16.dp),
                    singleLine = true,
                    shape = MaterialTheme.shapes.large,
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                    contentPadding = contentPadding(
                        start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp
                    )
                )
            }


            // Total Score and Bin Icon
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = "Total: $totalScore", softWrap = true, modifier = modifier.weight(10f)
                )
                IconButton(
                    onClick = {
                        onResetPlayerScore(player.id)
                    }, modifier = modifier.size(36.dp)

                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.delete_sweep),
                        contentDescription = "Delete Score Button",
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline)


            // Score Card Layout
            if (validScoreCard) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(vertical = 4.dp),
                ) {
                    items(scores, key = { it.scoreId }) { item ->

                        ScoreLine(
                            scoreIndex = item.scoreId,
                            score = item.scores,
                            checkScoreAdd = checkScoreAdd,
                            onChangeScore = onChangeScore,
                            onDeleteScore = onDeleteScore,
                            formatScoreAdd = formatScoreAdd
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
                        text = "No Scores Added", textAlign = TextAlign.Center
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
        }


        // Score Addition Buttons
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier.padding(top = 16.dp)
        ) {
            var scoreAdd by rememberSaveable { mutableStateOf("0") }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = {
                        if (checkScoreAdd(scoreAdd)) {
                            scoreAdd = (scoreAdd.toInt() - 1).toString()
                        } else {
                            scoreAdd = "0"
                        }
                    },
                    modifier = modifier.weight(9f),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Add Score Button",
                    )
                }
                CompactOutlinedTextField(
                    value = scoreAdd,
                    onValueChange = {
                        scoreAdd = formatScoreAdd(it)
                    },
                    label = { Text("") },
                    modifier = modifier
                        .widthIn(max = 72.dp)
                        .heightIn(min = 40.dp)
                        .weight(10f),
                    singleLine = true,
                    shape = MaterialTheme.shapes.extraSmall,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface
                    ),
                )
                IconButton(
                    onClick = {
                        if (checkScoreAdd(scoreAdd)) {
                            scoreAdd = (scoreAdd.toInt() + 1).toString()
                        } else {
                            scoreAdd = "0"
                        }
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
                if (checkScoreAdd(scoreAdd)) {
                    onAddScore(player.id, scoreAdd)
                } else {
                    scoreAdd = "0"
                }
            }) {
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
    scoreIndex: Int,
    score: String,
    checkScoreAdd: (String) -> Boolean,
    onChangeScore: (Int, String) -> Unit,
    onDeleteScore: (Int) -> Unit,
    formatScoreAdd: (String) -> String
) {

    var currentScore by remember { mutableStateOf(score) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 1.dp)
    ) {
        IconButton(
            onClick = {
                if (checkScoreAdd(currentScore)) {
                    currentScore = (currentScore.toInt() - 1).toString()
                    onChangeScore(scoreIndex, currentScore)
                } else {
                    currentScore = "0"
                    onChangeScore(scoreIndex, currentScore)
                }

            }, modifier = modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Add Score Button",
            )
        }
        CompactOutlinedTextField(
            value = currentScore,
            onValueChange = {
                val previous = it
                if (checkScoreAdd(it)) {
                    currentScore = formatScoreAdd(it)
                    onChangeScore(scoreIndex, it)
                } else {
                    val format = formatScoreAdd(it)
                    if (format == "") {
                        currentScore = previous
                    } else {
                        currentScore = format
                    }
                }
            },
            label = { Text("") },
            modifier = modifier
                .height(36.dp)
                .weight(1f),
            singleLine = true,
            shape = MaterialTheme.shapes.extraSmall,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                color = MaterialTheme.colorScheme.onSurface
            ),
        )
        IconButton(
            onClick = {
                if (checkScoreAdd(currentScore)) {
                    currentScore = (currentScore.toInt() + 1).toString()
                    onChangeScore(scoreIndex, currentScore)
                } else {
                    currentScore = "0"
                    onChangeScore(scoreIndex, currentScore)
                }
            },
            modifier = modifier.size(32.dp),

            ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Add Score Button",
            )
        }
        IconButton(
            onClick = {
                onDeleteScore(scoreIndex)
                currentScore = score
            },
            modifier = modifier
                .size(18.dp)
                .padding(start = 4.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete Score Button",
            )
        }
    }
}