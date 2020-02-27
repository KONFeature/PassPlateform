package com.nivelais.kpass.domain.entities

import java.io.Serializable

// Represent an entry in a kfile
interface EntryEntity : Serializable

// Represent a folder in a kfile
data class FolderEntity(
    val name: String,
    val entries: List<EntryEntity>?
) : EntryEntity, Serializable

// Represent a file in a kfile
data class FileEntity(
    val name: String,
    val pass: String?
) : EntryEntity, Serializable