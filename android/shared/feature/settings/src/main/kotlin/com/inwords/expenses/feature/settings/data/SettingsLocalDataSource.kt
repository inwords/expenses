package com.inwords.expenses.feature.settings.data

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream


internal class SettingsLocalDataSource(context: Context) {

    private val Context.settingsDataStore: DataStore<Settings> by dataStore(
        fileName = "settings.pb",
        serializer = SettingsSerializer()
    )

    private val settingsDataStore: DataStore<Settings> by lazy { context.settingsDataStore }

    suspend fun setCurrentEventId(eventId: Long) {
        settingsDataStore.updateData { currentSettings ->
            currentSettings.toBuilder()
                .setCurrentEventId(eventId)
                .build()
        }
    }

    fun getCurrentEventId(): Flow<Long?> {
        return settingsDataStore.data.map { settings -> settings.currentEventId.takeIf { it != -1L } }
    }

    private class SettingsSerializer : Serializer<Settings> {

        override val defaultValue: Settings = Settings.newBuilder()
            .setCurrentEventId(-1)
            .build()

        override suspend fun readFrom(input: InputStream): Settings {
            try {
                return Settings.parseFrom(input)
            } catch (exception: InvalidProtocolBufferException) {
                throw CorruptionException("Cannot read proto.", exception)
            }
        }

        override suspend fun writeTo(t: Settings, output: OutputStream) = t.writeTo(output)
    }
}