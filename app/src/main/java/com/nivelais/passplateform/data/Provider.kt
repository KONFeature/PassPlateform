package com.nivelais.passplateform.data

import com.nivelais.passplateform.R

enum class Provider(val titleId: Int,
                    val iconId: Int,
                    val intentRequestCode: Int,
                    val available: Boolean) {
    FILE_SYSTEM(R.string.provider_filesystem, R.drawable.ic_provider_file_system, 131, true),
    GDRIVE(R.string.provider_gdrive, R.drawable.ic_provider_google_drive, 132, false),
    HTTP(R.string.provider_http, R.drawable.ic_provider_http, 133, false),
    SSH(R.string.provider_ssh, R.drawable.ic_provider_ssh, 134, false);

    companion object {
        // Retreive a provider from his hashcode
        fun fromHashcode(hash: Int?): Provider =
            values().firstOrNull { provider -> provider.hashCode() == hash }?:let { FILE_SYSTEM }


        // Return a list of all the intent request code
        fun requestCodes() : List<Int> =
            values().map { provider -> provider.intentRequestCode }

    }
}

