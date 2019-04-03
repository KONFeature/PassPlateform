package com.nivelais.passplateform.data.local.entities

import android.net.Uri
import com.nivelais.passplateform.utils.Provider
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter
import java.util.*

@Entity
data class PassDatabase(
    var name: String,
    @Convert(converter = UriConvert::class, dbType = String::class)
    var localPath: Uri = Uri.EMPTY,
    @Convert(converter = UriConvert::class, dbType = String::class)
    var distPath: Uri = Uri.EMPTY,
    @Convert(converter = ProviderConvert::class, dbType = Int::class)
    var provider: Provider = Provider.FILE_SYSTEM,
    var lastOpen: Date
) {
    // Complex properties
    @Id var id: Long = 0
}

// Convert for uri properties
private class UriConvert : PropertyConverter<Uri, String> {
    override fun convertToEntityProperty(databaseValue: String?): Uri = Uri.parse(databaseValue)

    override fun convertToDatabaseValue(entityProperty: Uri?): String = entityProperty.toString()
}

// Convert for provider properties
private class ProviderConvert: PropertyConverter<Provider, Int> {
    override fun convertToDatabaseValue(entityProperty: Provider?): Int
            = entityProperty.hashCode()

    override fun convertToEntityProperty(databaseValue: Int?): Provider
            = Provider.fromHashcode(databaseValue)
}