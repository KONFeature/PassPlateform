package com.nivelais.passplateform.utils

import com.nivelais.passplateform.R

enum class Provider(val titleId: Int,
                    val iconId: Int) {
    FILE_SYSTEM(R.string.provider_filesystem, R.drawable.ic_provider_file_system),
    GDRIVE(R.string.provider_gdrive, R.drawable.ic_provider_google_drive)
}

