package com.nivelais.kpass.data.repository

import com.nivelais.kpass.data.db.KFileDataEntity
import com.nivelais.kpass.data.db.MyObjectBox
import com.nivelais.kpass.data.db.ObjectBox
import com.nivelais.kpass.data.helper.KeepassHelper
import com.nivelais.kpass.data.mapper.KFileDataEntityMapper
import com.nivelais.kpass.domain.entities.KFileEntity
import com.nivelais.kpass.domain.exception.CreateException
import com.nivelais.kpass.domain.exception.RemoveKFileException
import com.nivelais.kpass.domain.repository.KFilesManagerRepository
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import java.io.File

class KFilesManagerRepositoryImpl(
    boxStore: BoxStore,
    internalDir: File
) : KFilesManagerRepository {

    companion object {
        const val TAG = "KFilesRepository"
        const val KFILE_EXTENSION = ".kdbx"
    }

    /**
     * Local storage for the newly created kfile
     */
    private val localStorage = File(internalDir, "kfiles")

    /**
     * Access to our database
     */
    private val dao: Box<KFileDataEntity> = boxStore.boxFor()

    /**
     * Mapper for our database entity
     */
    private val entityMapper = KFileDataEntityMapper()

    override fun listKFiles(): List<KFileEntity> =
        entityMapper.mapList(dao.all)

    override fun createKFile(name: String, pass: String): KFileEntity {
        // check the name for file extension
        val finalName = if (!name.endsWith(KFILE_EXTENSION)) name + KFILE_EXTENSION else name
        val finalFile = File(localStorage, finalName)
        // Check if the file already exist
        if (finalFile.exists()) {
            throw CreateException(
                "The file already exist",
                null
            )
        }
        // save the kfile to database
        val kfile =
            KFileDataEntity(
                name = name,
                path = finalFile.absolutePath
            )
        dao.put(kfile)
        // Try to create the output file
        try {
            KeepassHelper.create(finalFile, pass)
        } catch (exception: Exception) {
            throw CreateException(
                exception.message ?: "Unknown error",
                exception
            )
        }
        // Return the new kfile
        return entityMapper.map(kfile)
    }

    override fun removeKFile(kfile: KFileEntity, removeFileToo: Boolean) {
        // If needed, remove file from storage
        var fileDeleted = true
        if (removeFileToo && kfile.path != null) {
            val file = File(kfile.path!!)
            fileDeleted = file.delete()
        }

        // If an error occured throw an exception
        if (kfile.path == null || !fileDeleted) {
            throw RemoveKFileException("Error during the local file deletation", null)
        }

        // Check if we can delete the kfile from db
        kfile.id?.let {
            dao.remove(it)
        } ?: run {
            throw RemoveKFileException("Unable to find the kfile to delete", null)
        }
    }
}