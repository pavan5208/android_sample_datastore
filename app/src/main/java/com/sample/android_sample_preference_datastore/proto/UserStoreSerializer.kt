package com.sample.android_sample_preference_datastore.proto

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.sample.android_sample_preference_datastore.UserStore
import java.io.InputStream
import java.io.OutputStream

object UserStoreSerializer : Serializer<UserStore> {

    override val defaultValue: UserStore = UserStore.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): UserStore {
        try {
            return UserStore.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserStore, output: OutputStream) = t.writeTo(output)

}