package com.sample.android_sample_preference_datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserRepoImpl(private val prefsDataStore: DataStore<Preferences>) :UserRepo {
    override suspend fun saveUserLoggedInState(state: Boolean) {
        prefsDataStore.edit { preferences ->
            preferences[PreferenceKeys.IS_USER_LOGGED_IN] = state
        }
    }

    override suspend fun getUserLoggedInState(): Flow<Boolean> {
      return  prefsDataStore.data
            .catch { exception ->
                // dataStore.data throws an IOException when an error is encountered when reading data
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                preferences[PreferenceKeys.IS_USER_LOGGED_IN]?:false
            }
    }
}