package com.nivelais.kpass.data.helper

import de.slackspace.openkeepass.KeePassDatabase
import de.slackspace.openkeepass.domain.KeePassFileBuilder
import java.io.File

/**
 * Helper to manipulate keepass file
 */
object KeepassHelper {

    /**
     * Create a keepass file
     */
    fun create(outputFile: File, password: String) {
        // Check if the parent folder exist, else create it
        if (outputFile.parentFile?.exists() == false) outputFile.parentFile?.mkdirs()

        val file = KeePassFileBuilder(outputFile.name).build()
        KeePassDatabase.write(file, password, outputFile.outputStream())
    }

}