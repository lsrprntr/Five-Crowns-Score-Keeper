package com.samplural.fivecrownsscorekeeper.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.samplural.fivecrownsscorekeeper.ui.AppViewModelProvider

@Composable
fun RankingScreen(
    modifier: Modifier = Modifier,
    viewModel: RankingScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onBackClick: () -> Unit,
) {

}