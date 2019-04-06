package com.nivelais.passplateform.data.local.entities

import android.net.Uri
import com.nivelais.passplateform.utils.Provider
import com.nivelais.passplateform.utils.ProviderConvert
import com.nivelais.passplateform.utils.UriConvert
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique
import io.objectbox.converter.PropertyConverter
import java.util.*

@Entity
data class PassDatabase(
    @Unique
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