package com.sample.android_sample_preference_datastore

import kotlinx.coroutines.flow.Flow

interface UserRepo {
    suspend fun saveUserLoggedInState(state:Boolean)
    suspend fun getUserLoggedInState(): Flow<Boolean>
}