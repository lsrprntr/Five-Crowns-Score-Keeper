package com.samplural.fivecrownsscorekeeper.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.samplural.fivecrownsscorekeeper.R
import com.samplural.fivecrownsscorekeeper.data.Players
import com.samplural.fivecrownsscorekeeper.data.Scores
import com.samplural.fivecrownsscorekeeper.data.UserPreferences
import com.samplural.fivecrownsscorekeeper.data.scoreSeperator
import com.samplural.fivecrownsscorekeeper.ui.AppViewModelProvider
import com.samplural.fivecrownsscorekeeper.ui.templates.CompactOutlinedTextField


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeApp(
    modifier: Modifier = Modifier,
    viewModel: HomeAppViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onSettingsClick: () -> Unit,
    onAboutClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val scoreUiState by viewModel.scoreUiState.collectAsState()
    val settingsUiState = viewModel.settingsUiState.collectAsState()

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
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.trophy),
                            contentDescription = "Rankings View Button"
                        )
                    }
                    IconButton(onClick = { dropDownMenuExpanded = !dropDownMenuExpanded }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "More Options",
                        )
                    }
                    IconButton(onClick = {
                        viewModel.updateBooleanWithKey(
                            "show_grid_view",
                            !settingsUiState.value.showGridView
                        )
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = if (settingsUiState.value.showGridView) R.drawable.column_view else R.drawable.grid_view),
                            contentDescription = "Switch View Layout Button"
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
                        DropdownMenuItem(onClick = {
                            dropDownMenuExpanded = false
                            onAboutClick()
                        }, text = { Text("About") }, leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "About Me Button",
                            )
                        })
                    }

                },
            )
        },
        bottomBar = { HorizontalDivider() }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            HomeBody(
                playersList = uiState.player,
                scoresList = scoreUiState.scores,
                settingsUiState = settingsUiState.value,
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

@OptIn(ExperimentalFoundationApi::class)
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
    settingsUiState: UserPreferences,
) {

    if (playersList.isNotEmpty()) {
        val state = remember {
            MutableTransitionState(false).apply {
                // Start the animation immediately.
                targetState = true
            }
        }
        AnimatedVisibility(
            visibleState = state,
            enter = fadeIn(),
            modifier = modifier.padding(vertical = 16.dp)
        ) {

            val showGridView = settingsUiState.showGridView
            if (showGridView) {
                LazyVerticalStaggeredGrid(
                    modifier = modifier.fillMaxSize(),
                    columns = StaggeredGridCells.Adaptive(160.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    content = {
                        items(count = playersList.size,
                            key = { playersList[it].id },
                            itemContent = { index ->
                                val player = playersList[index]
                                Box(
                                    modifier = Modifier
                                        .animateItemPlacement()
                                        .wrapContentWidth(),
                                    contentAlignment = Alignment.TopCenter
                                ) {
                                    val filteredScores =
                                        scoresList.filter { it.playerId == player.id }
                                    val cardState = remember {
                                        MutableTransitionState(false).apply {
                                            // Start the animation immediately.
                                            targetState = true
                                        }
                                    }
                                    AnimatedVisibility(
                                        visibleState = cardState, enter = fadeIn(),
                                    ) {
                                        PlayerCard(
                                            player = player,
                                            scores = filteredScores,
                                            settingsUiState = settingsUiState,
                                            onDeletePlayer = onDeletePlayer,
                                            onNameChange = onNameChange,
                                            onAddScore = onAddScore,
                                            checkScoreAdd = checkScoreAdd,
                                            onChangeScore = onChangeScore,
                                            formatScoreAdd = formatScoreAdd,
                                            onDeleteScore = onDeleteScore,
                                            onResetPlayerScore = onResetPlayerScore,
                                        )


                                    }
                                }
                            }

                        )
                    },
                )
            } else {
                LazyRow(
                    modifier = modifier.padding(start = 4.dp, end = 4.dp, bottom = 4.dp)
                ) {

                    items(count = playersList.size,
                        key = { playersList[it].id },
                        itemContent = { index ->
                            val player = playersList[index]
                            Box(
                                modifier = modifier.animateItemPlacement(),
                            ) {
                                val filteredScores = scoresList.filter { it.playerId == player.id }
                                val cardState = remember {
                                    MutableTransitionState(false).apply {
                                        // Start the animation immediately.
                                        targetState = true
                                    }
                                }
                                AnimatedVisibility(
                                    visibleState = cardState, enter = fadeIn(),
                                ) {
                                    PlayerCard(
                                        player = player,
                                        scores = filteredScores,
                                        settingsUiState = settingsUiState,
                                        onDeletePlayer = onDeletePlayer,
                                        onNameChange = onNameChange,
                                        onAddScore = onAddScore,
                                        checkScoreAdd = checkScoreAdd,
                                        onChangeScore = onChangeScore,
                                        formatScoreAdd = formatScoreAdd,
                                        onDeleteScore = onDeleteScore,
                                        onResetPlayerScore = onResetPlayerScore,
                                    )

                                }

                            }

                        })
                }
            }


        }
    } else {
        // No Players Added
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
                enter = fadeIn(spring(stiffness = 12f)),
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


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerCard(
    modifier: Modifier = Modifier,
    player: Players,
    onDeletePlayer: (Int) -> Unit,
    onNameChange: (Int, String) -> Unit,
    onAddScore: (Int, String) -> Unit,
    checkScoreAdd: (String) -> Boolean,
    onChangeScore: (Int, String) -> Unit,
    formatScoreAdd: (String) -> String,
    onDeleteScore: (Int) -> Unit,
    onResetPlayerScore: (Int) -> Unit,
    scores: List<Scores>,
    settingsUiState: UserPreferences,
) {
    var showEditOptions: Boolean by remember { mutableStateOf(false) }

    val justScores = scores.map { it.scores }
    val playerScores = justScores.joinToString(scoreSeperator)
    val validScoreCard = playerScores.isNotEmpty() || (playerScores == "0")
    val totalScore = if (validScoreCard) {
        justScores.sumOf { it.toInt() }
    } else {
        0
    }

    Box(
        contentAlignment = Alignment.TopEnd,
    ) {

        Card(
            modifier = Modifier
                .padding(8.dp)
                .width(180.dp)
                .animateContentSize(),
        ) {


            val showIncrementArrows = settingsUiState.showIncrementArrows
            val showDeleteRows = if (showEditOptions) {
                true
            } else {
                settingsUiState.showDeleteRows
            }
            val showRoundLabels = settingsUiState.showRoundLabels
            val showEditNumbers = if (showEditOptions) {
                true
            } else {
                settingsUiState.showExpandedScores
            }
            val showScoreDividers = settingsUiState.showScoreDividers
            val showAddArrows = settingsUiState.showAddArrows
            val alwaysExpand = !settingsUiState.showGridView
            if (alwaysExpand) {
                showEditOptions = true
            }
            val startNumber = settingsUiState.startNumber

            // Player Details And Score Card
            Column(
                modifier = modifier
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    .animateContentSize()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Top,
            ) {

                var textName by rememberSaveable { mutableStateOf(player.name) }
                Column(
                    modifier = Modifier

                ) {
                    // Player Name Box
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
                        modifier = Modifier.height(32.dp),
                        singleLine = true,
                        shape = MaterialTheme.shapes.large,
                        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                        contentPadding = contentPadding(
                            start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp
                        )
                    )

                    // Total Score and Bin Icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total: $totalScore",
                            softWrap = true,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .weight(1f)
                        )
                        if (showEditOptions) {
                            if (!alwaysExpand) {
                                IconButton(
                                    onClick = { showEditOptions = false },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.expand_less),
                                        contentDescription = "Edit Player Button",
                                    )
                                }
                            }
                        } else {
                            // Expand Key
                            IconButton(
                                onClick = { showEditOptions = true },
                                modifier = Modifier.size(30.dp)
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.expand_more),
                                    contentDescription = "Edit Player Button",
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                        }
                    }

                    // Pull Out Edit Options
                    if (showEditOptions) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(modifier = modifier.size(32.dp), onClick = {
                                showEditOptions = false
                                onResetPlayerScore(player.id)
                            }) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.delete_sweep),
                                    contentDescription = "Delete Score Button",
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline)

                val layoutBoundModifier =
                    if (settingsUiState.showGridView) Modifier.height(194.dp) else Modifier.weight(
                        1f
                    )


                // Score Card Layout
                if (showEditOptions) {
                    if (validScoreCard) {


                        LazyColumn(
                            modifier = layoutBoundModifier
                                .padding(vertical = 4.dp)
                        ) {
                            items(count = scores.size,
                                key = { scores[it].scoreId },
                                itemContent = { index ->
                                    val item = scores[index]
                                    Box(
                                        contentAlignment = Alignment.BottomCenter,
                                        modifier = Modifier.animateItemPlacement()
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.CenterStart,
                                        ) {

                                            ScoreLine(
                                                scoreIndex = item.scoreId,
                                                score = item.scores,
                                                roundText = "${index + startNumber}:",
                                                checkScoreAdd = checkScoreAdd,
                                                onChangeScore = onChangeScore,
                                                onDeleteScore = onDeleteScore,
                                                formatScoreAdd = formatScoreAdd,
                                                showRoundLabels = showRoundLabels,
                                                showIncrementArrows = showIncrementArrows,
                                                showDeleteRows = showDeleteRows,
                                                showEditNumbers = showEditNumbers
                                            )
                                        }
                                        if (showScoreDividers) {
                                            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                                        }
                                    }

                                })
                        }
                    } else {
                        // No Scores Added
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = layoutBoundModifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "No Scores\nAdded", textAlign = TextAlign.Center
                            )
                        }
                    }
                }


                HorizontalDivider(color = MaterialTheme.colorScheme.outline)

                // Score Addition Buttons
                Text(
                    "${scores.size} ${if (scores.size == 1) "score" else "scores"}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall
                )
                ScoreAdditionBottom(
                    modifier = modifier.wrapContentHeight(),
                    checkScoreAdd = checkScoreAdd,
                    formatScoreAdd = formatScoreAdd,
                    onAddScore = onAddScore,
                    player = player,
                    showArrows = showAddArrows
                )
            }
        }
        // Delete Player Button
        IconButton(
            onClick = {
                onDeletePlayer(player.id)
            },
            modifier = Modifier
                .size(16.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.remove_circle),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
            )
        }
    }
}

@Composable
fun ScoreLine(
    modifier: Modifier = Modifier,
    scoreIndex: Int,
    score: String,
    roundText: String,
    checkScoreAdd: (String) -> Boolean,
    onChangeScore: (Int, String) -> Unit,
    onDeleteScore: (Int) -> Unit,
    formatScoreAdd: (String) -> String,
    showRoundLabels: Boolean,
    showIncrementArrows: Boolean,
    showDeleteRows: Boolean,
    showEditNumbers: Boolean
) {

    var currentScore by remember { mutableStateOf(score) }

    val state = remember {
        MutableTransitionState(false).apply {
            // Start the animation immediately.
            targetState = true
        }
    }
    AnimatedVisibility(
        visibleState = state,
        enter = fadeIn(),
        modifier = modifier.wrapContentHeight()
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(vertical = 4.dp)
        ) {

            if (showIncrementArrows)
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


            if (!showEditNumbers) {
                Text(
                    text = currentScore,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier
                        .padding(4.dp)
                        .width(36.dp)
                )
            } else {
                TextFieldButton(
                    currentText = score,
                    onSaveText = {
                        onChangeScore(scoreIndex, it)
                    },
                    formatText = formatScoreAdd,
                    alertTextTitle = "Edit Score",
                    modifier = modifier.padding(horizontal = 4.dp)
                )
            }
            if (showIncrementArrows) {
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
            }
        }

        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth(),
        ) {
            if (showDeleteRows) {
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
        Box(
            modifier = Modifier.height(40.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (showRoundLabels) {
                Text(
                    text = roundText,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }

}

@Composable
private fun ScoreAdditionBottom(
    modifier: Modifier,
    checkScoreAdd: (String) -> Boolean,
    formatScoreAdd: (String) -> String,
    onAddScore: (Int, String) -> Unit,
    player: Players,
    showArrows: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .padding(top = 8.dp)

    ) {
        var scoreAdd by rememberSaveable { mutableStateOf("0") }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (showArrows) {
                IconButton(
                    onClick = {
                        if (checkScoreAdd(scoreAdd)) {
                            scoreAdd = (scoreAdd.toInt() - 1).toString()
                        } else {
                            scoreAdd = "0"
                        }
                    },
                    modifier = Modifier.weight(9f),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Add Score Button",
                    )
                }
            }
            CompactOutlinedTextField(
                value = scoreAdd,
                onValueChange = {
                    scoreAdd = formatScoreAdd(it)
                },
                label = { Text("") },
                modifier = Modifier
                    .height(40.dp)
                    .width(60.dp)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            scoreAdd = ""
                        } else {
                            if (scoreAdd == "") {
                                scoreAdd = "0"
                            } else {
                                scoreAdd = formatScoreAdd(scoreAdd)
                            }
                        }
                    },
                singleLine = true,
                shape = MaterialTheme.shapes.extraSmall,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(
                    textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface
                ),
            )
            if (showArrows) {
                IconButton(
                    onClick = {
                        if (checkScoreAdd(scoreAdd)) {
                            scoreAdd = (scoreAdd.toInt() + 1).toString()
                        } else {
                            scoreAdd = "0"
                        }
                    },
                    modifier = Modifier.weight(9f),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Add Score Button",
                    )
                }
            }
        }

        val showReset = (scoreAdd == "" || scoreAdd == "-")
        AnimatedVisibility(showReset) {
            IconButton(onClick = {
                scoreAdd = "0"
            }) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Reset Score Add to zero",
                    modifier = modifier.size(32.dp)
                )
            }
        }
        AnimatedVisibility(!showReset) {
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