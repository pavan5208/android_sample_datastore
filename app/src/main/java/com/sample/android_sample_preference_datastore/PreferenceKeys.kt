package com.sample.android_sample_preference_datastore

import androidx.datastore.preferences.core.booleanPreferencesKey

object PreferenceKeys {
    const val PREFERENCES_NAME_USER =  "sample_datastore_prefs"

    val IS_USER_LOGGED_IN = booleanPreferencesKey("is_user_logged_in")
}
