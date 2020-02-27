package com.nivelais.kpass.domain.repository

import com.nivelais.kpass.domain.entities.KFileEntity

/**
 * Repository for all the actions about a KFile management (create, remove import ...)
 */
interface KFilesManagerRepository {

    /**
     * List all the known kfiles
     */
    fun listKFiles() : List<KFileEntity>

    /**
     * Create a new kfile
     */
    fun createKFile(name: String, pass: String) : KFileEntity

    /**
     * Remove a kfile
     */
    fun removeKFile(kfile: KFileEntity, removeFileToo: Boolean)

}