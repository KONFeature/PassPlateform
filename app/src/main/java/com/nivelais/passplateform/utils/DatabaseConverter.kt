package com.nivelais.passplateform.utils

import android.net.Uri
import io.objectbox.converter.PropertyConverter

// Convert for uri properties
class UriConvert : PropertyConverter<Uri, String> {
    override fun convertToEntityProperty(databaseValue: String?): Uri = Uri.parse(databaseValue)

    override fun convertToDatabaseValue(entityProperty: Uri?): String = entityProperty.toString()
}

// Convert for provider properties
class ProviderConvert: PropertyConverter<Provider, Int> {
    override fun convertToDatabaseValue(entityProperty: Provider?): Int
            = entityProperty.hashCode()

    override fun convertToEntityProperty(databaseValue: Int?): Provider
            = Provider.fromHashcode(databaseValue)
}