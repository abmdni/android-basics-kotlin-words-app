package com.example.wordsapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.prefs.Preferences
import kotlin.coroutines.coroutineContext

private const val LAYOUT_PREFERENCES_NAME = "layout_preferences"

private val Context.dataStore /*: DataStore<Preferences> */by preferencesDataStore(
    name = LAYOUT_PREFERENCES_NAME
)

class SettingsDataStore(context: Context) {
    private val IS_LINEAR_LAYOUT_MANAGER = booleanPreferencesKey("isLinearLayoutManager")

    val preferenceFlow :Flow<Boolean> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map{ preferences ->
            preferences[IS_LINEAR_LAYOUT_MANAGER]?:true
    }

    suspend fun saveLayoutToPreferencesStore(isLinearLayout: Boolean, context: Context){
        context.dataStore.edit { preferences ->
            preferences[IS_LINEAR_LAYOUT_MANAGER] = isLinearLayout
        }
    }

}