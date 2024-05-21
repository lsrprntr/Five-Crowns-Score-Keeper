package com.samplural.fivecrownsscorekeeper.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.samplural.fivecrownsscorekeeper.ui.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(topBar = {
        TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ), title = {
            Text("Settings")
        }, navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                "Individual Score Row Layout",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                color = MaterialTheme.colorScheme.outline,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                SettingsBoolItem(title = "Show Score Rows",
                    checked = uiState.showScoreRows,
                    onCheckedChange = { viewModel.updateBooleanWithKey("show_score_rows", it) })
                SettingsBoolItem(title = "Show Increment Arrows",
                    checked = uiState.showIncrementArrows,
                    onCheckedChange = {
                        viewModel.updateBooleanWithKey(
                            "show_increment_arrows",
                            it
                        )
                    })
                SettingsBoolItem(title = "Show Delete Row Icons",
                    checked = uiState.showDeleteRows,
                    onCheckedChange = { viewModel.updateBooleanWithKey("show_delete_rows", it) })
                SettingsBoolItem(title = "Show Round Labels",
                    checked = uiState.showRoundLabels,
                    onCheckedChange = { viewModel.updateBooleanWithKey("show_round_labels", it) })
                SettingsBoolItem(title = "Show Edit Score Boxes Always",
                    checked = uiState.showEditNumbers,
                    onCheckedChange = { viewModel.updateBooleanWithKey("show_edit_numbers", it) })
                SettingsBoolItem(title = "Show Score Row Dividers",
                    checked = uiState.showScoreDividers,
                    onCheckedChange = { viewModel.updateBooleanWithKey("show_score_dividers", it) })
            }

            Text(
                "Bottom Score Adder Layout",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                color = MaterialTheme.colorScheme.outline,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                SettingsBoolItem(title = "Show Increment Arrows",
                    checked = uiState.showAddArrows,
                    onCheckedChange = { viewModel.updateBooleanWithKey("show_add_arrows", it) })
            }
        }
    }
}

@Composable
fun SettingsBoolItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        Switch(
            checked = checked, onCheckedChange = {
                onCheckedChange(it)
            }, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
    HorizontalDivider()

}


