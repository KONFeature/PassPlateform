package com.nivelais.kpass.data.db

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.io.Serializable
import java.util.*

@Entity
data class KFileDataEntity(
    @Id
    var id: Long = 0,

    /**
     * Name of the kfile
     */
    var name: String? = null,

    /**
     * Path to access the kfile (relative to the provider)
     */
    var path: String? = null,

    /**
     * The pass of the kfile from the internal app storage
     */
    var localPath: String? = null,

    /**
     * Last time the kfile was openned
     */
    var lastOpen: Date? = null,

    /**
     * Was the kfile locally updated ?
     */
    var localUpdate: Boolean? = false

)