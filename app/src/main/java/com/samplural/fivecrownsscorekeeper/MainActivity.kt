package com.samplural.fivecrownsscorekeeper

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.compose.AppTheme
import com.samplural.fivecrownsscorekeeper.ui.NavApp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)//Depreciated but works better than the manifest

        setContent {
            AppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                        .imePadding(), //So keyboard does not cover the content
                ) { paddingValues ->
                    NavApp(
                        modifier = Modifier.fillMaxSize()
                            .padding(paddingValues)
                    )
                }
            }
        }
    }
}


