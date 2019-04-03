package com.nivelais.passplateform.utils

import com.nivelais.passplateform.R
import io.objectbox.converter.PropertyConverter

enum class Provider(val titleId: Int,
                    val iconId: Int,
                    val intentRequestCode: Int) {
    FILE_SYSTEM(R.string.provider_filesystem, R.drawable.ic_provider_file_system, 131),
    GDRIVE(R.string.provider_gdrive, R.drawable.ic_provider_google_drive, 132);

    companion object {
        // Retreive a provider from his hashcode
        fun fromHashcode(hash: Int?): Provider =
            values().firstOrNull { provider -> provider.hashCode() == hash }?:let { FILE_SYSTEM }


        // Return a list of all the intent request code
        fun requestCodes() : List<Int> =
            values().map { provider -> provider.intentRequestCode }

    }
}

