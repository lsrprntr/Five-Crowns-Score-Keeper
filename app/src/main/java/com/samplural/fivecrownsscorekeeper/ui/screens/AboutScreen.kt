package com.samplural.fivecrownsscorekeeper.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("About Me")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val state = remember {
                MutableTransitionState(false).apply {
                    // Start the animation immediately.
                    targetState = true
                }
            }
            AnimatedVisibility(
                visibleState = state,
                enter = fadeIn(spring(10f))
            ) {
                Column(
                    modifier.padding(60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Created by Samplural.",
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "This is a free app made from in my free time for a friend.",
                        textAlign = TextAlign.Center
                    )
                    val annotatedString = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onPrimaryContainer)) {
                            append("My ")
                        }

                        pushStringAnnotation(tag = "website", annotation = "https://lsrprntr.github.io/")
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                            append("website")
                        }
                        pop()
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onPrimaryContainer)) {
                            append(" and ")
                        }

                        pushStringAnnotation(
                            tag = "github",
                            annotation = "https://github.com/lsrprntr"
                        )

                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                            append("GitHub")
                        }

                        pop()
                    }

                    val uriHandler = LocalUriHandler.current

                    ClickableText(
                        text = annotatedString,
                        style = TextStyle(textAlign = TextAlign.Center),
                        onClick = { offset ->
                            annotatedString.getStringAnnotations(
                                tag = "website",
                                start = offset,
                                end = offset
                            ).firstOrNull()?.let {
                                uriHandler.openUri(it.item)
                            }

                            annotatedString.getStringAnnotations(
                                tag = "github",
                                start = offset,
                                end = offset
                            ).firstOrNull()?.let {
                                uriHandler.openUri(it.item)
                            }
                        },
                    )
                    AnimatedVisibility(
                        visibleState = state,
                        enter = fadeIn(spring(1000f))
                    ) {
                        Text("I need a job.")
                    }
                }

            }
        }

    }
}