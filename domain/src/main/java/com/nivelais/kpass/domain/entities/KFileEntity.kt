package com.nivelais.kpass.domain.entities

import java.io.Serializable
import java.util.*

data class KFileEntity(
    val id: Long?,
    val name: String,
    val path: String?,
    val lastOpen: Date?,
    val entries: List<EntryEntity>?
) : Serializable