package com.sample.android_sample_preference_datastore.proto

import kotlinx.coroutines.flow.Flow

interface ProtoUserRepo {
    suspend fun saveUserLoggedInState(state:Boolean)
    suspend fun getUserLoggedInState(): Flow<Boolean>
}