package com.maisel.data.database

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.maisel.domain.database.ApplicationSetting
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object SettingsSerializer : Serializer<ApplicationSetting> {

    /**
     * Default value of the settings for initialization
     */
    override val defaultValue: ApplicationSetting = ApplicationSetting.default

    /**
     * Reading the inputstream of the stored file
     */
    override suspend fun readFrom(input: InputStream): ApplicationSetting {
        try {
            return Json.decodeFromString(
                deserializer = ApplicationSetting.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (exception: SerializationException) {
            throw CorruptionException("Error occurred during decoding the storage", exception)
        }
    }

    /**
     * Writing the the output stream with actual datatype
     */
    override suspend fun writeTo(
        t: ApplicationSetting,
        output: OutputStream
    ) = output.write(
        Json.encodeToString(serializer = ApplicationSetting.serializer(), value = t).toByteArray()
    )

}

/**
 * Context property delegate with the datastore
 */
val Context.settingsDataStore: DataStore<ApplicationSetting> by dataStore(
    fileName = "settings.json",
    serializer = SettingsSerializer
)
