package com.samplural.fivecrownsscorekeeper.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.samplural.fivecrownsscorekeeper.data.SettingsRepository.PreferencesKeys.SHOW_INCREMENT_ARROWS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val PREFERENCES_NAME = "settings"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

data class UserPreferences(
    val showIncrementArrows: Boolean = true,
    val showDeleteRows: Boolean = false,
    val showRoundLabels: Boolean = true,
    val showExpandedScores: Boolean = true,
    val showEditNumbers: Boolean = false,
    val showScoreDividers: Boolean = true,
    val showAddArrows: Boolean = true,
    val showScoreRows: Boolean = true,
    val showGridView: Boolean = false,
    val startNumber: Int = 1,

    ) {
}

class SettingsRepository(
    private val dataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val SHOW_INCREMENT_ARROWS = booleanPreferencesKey("show_increment_arrows")
        val SHOW_DELETE_ROWS = booleanPreferencesKey("show_delete_rows")
        val SHOW_ROUND_LABELS = booleanPreferencesKey("show_round_labels")
        val SHOW_EXPANDED_SCORES = booleanPreferencesKey("show_expanded_scores")
        val SHOW_EDIT_NUMBERS = booleanPreferencesKey("show_edit_numbers")
        val SHOW_SCORE_DIVIDERS = booleanPreferencesKey("show_score_dividers")
        val SHOW_ADD_ARROWS = booleanPreferencesKey("show_add_arrows")
        val SHOW_SCORE_ROWS = booleanPreferencesKey("show_score_rows")
        val SHOW_GRID_VIEW = booleanPreferencesKey("show_grid_view")
        val START_NUMBER = intPreferencesKey("start_number")


    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data.map { preferences ->
        val showIncrementArrows = preferences[SHOW_INCREMENT_ARROWS] ?: true
        val showDeleteRows = preferences[PreferencesKeys.SHOW_DELETE_ROWS] ?: true
        val showRoundLabels = preferences[PreferencesKeys.SHOW_ROUND_LABELS] ?: true
        val showExpandedScores = preferences[PreferencesKeys.SHOW_EXPANDED_SCORES] ?: true
        val showEditNumbers = preferences[PreferencesKeys.SHOW_EDIT_NUMBERS] ?: false
        val showScoreDividers = preferences[PreferencesKeys.SHOW_SCORE_DIVIDERS] ?: true
        val showAddArrows = preferences[PreferencesKeys.SHOW_ADD_ARROWS] ?: true
        val showScoreRows = preferences[PreferencesKeys.SHOW_SCORE_ROWS] ?: true
        val showGridView = preferences[PreferencesKeys.SHOW_GRID_VIEW] ?: false
        val startNumber = preferences[PreferencesKeys.START_NUMBER] ?: 1

        // Return for map
        UserPreferences(
            showIncrementArrows,
            showDeleteRows,
            showRoundLabels,
            showExpandedScores,
            showEditNumbers,
            showScoreDividers,
            showAddArrows,
            showScoreRows,
            showGridView,
            startNumber
        )
    }

    suspend fun updateBooleanWithKey(key: String, bool: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = bool
        }
    }
    suspend fun updateIntWithKey(key: String, num: Int) {
        dataStore.edit { preferences ->
            preferences[intPreferencesKey(key)] = num
        }
    }

    suspend fun resetSettings() {
        dataStore.edit {
            it.clear()
        }
    }

}


