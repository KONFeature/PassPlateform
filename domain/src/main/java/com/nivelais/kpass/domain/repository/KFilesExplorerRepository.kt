package com.nivelais.kpass.domain.repository

import com.nivelais.kpass.domain.entities.EntryEntity
import com.nivelais.kpass.domain.entities.FolderEntity
import com.nivelais.kpass.domain.entities.KFileEntity

/**
 * Repository for all the actions about a KFile entries (list, add etc)
 */
interface KFilesExplorerRepository {

    /**
     * Open a new Kfile
     */
    fun openKFile(kfile: KFileEntity, pass: String) : KFileEntity

    /**
     * List the current entries
     */
    fun currentEntries() : List<EntryEntity>?

    /**
     * Open a folder of a kfile entity
     */
    fun openFolder(folder: FolderEntity) : List<EntryEntity>?
}