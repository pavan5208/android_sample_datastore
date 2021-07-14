package com.sample.android_sample_preference_datastore.proto

import androidx.datastore.core.DataStore
import com.sample.android_sample_preference_datastore.UserStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class ProtoUserRepoImpl(private val protoDataStore: DataStore<UserStore>) :ProtoUserRepo {
    override suspend fun saveUserLoggedInState(state: Boolean) {
        protoDataStore.updateData {store ->
            store.toBuilder()
                .setIsLoggedIn(state)
                .build()
        }
    }

    override suspend fun getUserLoggedInState(): Flow<Boolean> {
      return  protoDataStore.data
            .catch { exception ->
                // dataStore.data throws an IOException when an error is encountered when reading data
                if (exception is IOException) {
                    emit(UserStore.getDefaultInstance())
                } else {
                    throw exception
                }
            }.map { protoBuilder ->
              protoBuilder.isLoggedIn
            }
    }
}