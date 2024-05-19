package com.samplural.fivecrownsscorekeeper.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private const val PREFERENCES_NAME = "settings"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

class SettingsRepository(
    private val dataStore: DataStore<Preferences>,
) {
    companion object {
        const val TAG = "UserPreferencesRepo"
        val SHOW_DETAILS = booleanPreferencesKey("showDetails")
        val SHOW_ARROWS = booleanPreferencesKey("showArrows")
    }

    // Properties
    val showDetails: Flow<Boolean> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[SHOW_DETAILS] ?: false
        }
    val showArrows: Flow<Boolean> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[SHOW_ARROWS] ?: false
        }

    // Write to functions
    suspend fun switchShowDetails(bool: Boolean) {
        dataStore.edit { preferences ->
            preferences[SHOW_DETAILS] = !bool
        }
    }

    suspend fun switchShowArrows(bool: Boolean) {
        dataStore.edit { preferences ->
            preferences[SHOW_ARROWS] = !bool
        }
    }
}

