package com.samplural.fivecrownsscorekeeper.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
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
                color = MaterialTheme.colorScheme.primary,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {

                SettingsBoolItem(
                    title = "Show Edit Increment Arrows",
                    checked = uiState.showIncrementArrows,
                    onCheckedChange = {
                        viewModel.updateBooleanWithKey(
                            "show_increment_arrows",
                            it
                        )
                    })

                SettingsBoolItem(title = "Show Round Labels",
                    checked = uiState.showRoundLabels,
                    onCheckedChange = { viewModel.updateBooleanWithKey("show_round_labels", it) })
                SettingsInputItem(
                    title = "Round Starting Number:",
                    buttonTitle = uiState.startNumber.toString(),
                    onSave = {
                        val num = viewModel.formatNumber(it)
                        viewModel.updateIntWithKey("start_number", num.toInt())
                    },
                    formatText = { viewModel.formatNumber(it) }
                )

                SettingsBoolItem(title = "Show Score Row Dividers",
                    checked = uiState.showScoreDividers,
                    onCheckedChange = { viewModel.updateBooleanWithKey("show_score_dividers", it) })
            }
            Text(
                "Bottom Score Adder Layout",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                color = MaterialTheme.colorScheme.primary,
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

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Button(
                    onClick = { viewModel.resetSettings() },
                    shape = RectangleShape
                ) {
                    Text("Reset to Default")
                }
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
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .weight(9f),
            style = MaterialTheme.typography.bodyLarge
        )
        Switch(
            checked = checked, onCheckedChange = {
                onCheckedChange(it)
            }, modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .weight(1f)
        )
    }
    HorizontalDivider()


}

@Composable
fun SettingsInputItem(
    title: String,
    buttonTitle: String,
    onSave: (String) -> Unit,
    formatText: (String) -> String,
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
        TextFieldButton(
            currentText = buttonTitle,
            onSaveText = onSave,
            formatText = formatText,
            alertTextTitle = "Please enter a number",
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .size(48.dp)
        )
    }
    HorizontalDivider()



}


@Composable
fun TextFieldButton(
    currentText: String,
    onSaveText: (String) -> Unit,
    formatText: (String) -> String,
    alertTextTitle: String,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = currentText
            )
        )
    }
    val focusRequester = remember { FocusRequester() }
    Button(
        onClick = { showDialog = true },
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        modifier = modifier
            .height(32.dp)
            .width(40.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = currentText,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = alertTextTitle) },
            text = {
                TextField(
                    value = textFieldValue,
                    onValueChange = {
                        textFieldValue = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        if (textFieldValue.text == "" || textFieldValue.text == "-") {
                            onSaveText("0")
                            showDialog = false
                        } else {
                            textFieldValue = TextFieldValue(formatText(textFieldValue.text))
                            onSaveText(formatText(textFieldValue.text))
                            showDialog = false
                        }
                    })
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        textFieldValue = TextFieldValue(formatText(textFieldValue.text))
                        onSaveText(formatText(textFieldValue.text))
                        showDialog = false
                    }
                ) {
                    Text(text = "Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text(text = "Cancel")
                }
            }
        )

        // Keyboard focus to end of text field
        LaunchedEffect(Unit) {
            textFieldValue = TextFieldValue(
                text = textFieldValue.text,
                selection = TextRange(0, textFieldValue.text.length)
            )
            focusRequester.requestFocus()

        }
    }
}

