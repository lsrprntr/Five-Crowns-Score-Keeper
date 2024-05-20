package com.samplural.fivecrownsscorekeeper.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val PREFERENCES_NAME = "settings"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

data class UserPreferences(
    val sortOrder: String,
    val showCompleted: Boolean,
)
class SettingsRepository(
    private val dataStore: DataStore<Preferences>
) {

    private object PreferencesKeys {
        val SORT_ORDER = stringPreferencesKey("sort_order")
        val SHOW_COMPLETED = booleanPreferencesKey("show_completed")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data.map { preferences ->
        val sortOrder = preferences[PreferencesKeys.SORT_ORDER] ?: "name"
        val showCompleted = preferences[PreferencesKeys.SHOW_COMPLETED] ?: false

        UserPreferences(sortOrder, showCompleted)
    }

    suspend fun updateShowCompleted(showCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_COMPLETED] = showCompleted
        }
    }

}


