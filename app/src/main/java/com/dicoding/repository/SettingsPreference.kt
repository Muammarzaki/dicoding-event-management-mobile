package com.dicoding.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SettingsPreference(private val dataStore: DataStore<Preferences>) {

    private val darkModeKey = booleanPreferencesKey("is_dark_mode_enable")
    private val notificationKey = booleanPreferencesKey("is_notification_enable")

    suspend fun enableDarkModePreference(state: Boolean) {
        dataStore.edit {
            it[darkModeKey] = state
        }
    }

    fun getDarkModePreference(): Flow<Boolean> {
        return dataStore.data
            .map {
                it[darkModeKey] ?: false
            }
    }

    suspend fun enableNotificationPreference(state: Boolean) {
        dataStore.edit {
            it[notificationKey] = state
        }
    }

    fun getNotificationPreference(): Flow<Boolean> {
        return dataStore.data
            .map {
                it[notificationKey] ?: false
            }
    }
}