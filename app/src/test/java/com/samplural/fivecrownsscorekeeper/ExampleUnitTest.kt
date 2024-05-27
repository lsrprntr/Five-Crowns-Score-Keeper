package com.samplural.fivecrownsscorekeeper

import androidx.compose.ui.test.junit4.createComposeRule
import com.example.compose.AppTheme
import com.samplural.fivecrownsscorekeeper.ui.NavApp
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
    // use createAndroidComposeRule<YourActivity>() if you need access to
    // an activity

    @Test
    fun myTest() {
        // Start the app
        composeTestRule.setContent {
            AppTheme {
                NavApp(

                )
            }
        }
    }
}