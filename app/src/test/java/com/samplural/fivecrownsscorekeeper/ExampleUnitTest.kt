package com.samplural.fivecrownsscorekeeper

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.compose.AppTheme
import com.samplural.fivecrownsscorekeeper.ui.screens.AboutScreen
import org.junit.Rule
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class MyComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun myTest() {
        // Start the app
        composeTestRule.setContent {
            AppTheme {
                AboutScreen(
                    onBackClick = {}
                )
            }
        }

        // Add assertions to the test
        composeTestRule.onNodeWithText("About Me").assertExists()
    }
}