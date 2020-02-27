package com.nivelais.kpass.domain.usecases.explorer

import com.nivelais.kpass.domain.common.Data
import com.nivelais.kpass.domain.common.Status
import com.nivelais.kpass.domain.entities.EntryEntity
import com.nivelais.kpass.domain.entities.KFileEntity
import com.nivelais.kpass.domain.repository.KFilesExplorerRepository
import com.nivelais.kpass.domain.usecases.UseCase

/**
 * When the user want to create a kfile
 */
class CurrentEntriesUseCase(private val explorerRepository: KFilesExplorerRepository) :
    UseCase<List<EntryEntity>, Unit>() {

    override suspend fun run(params: Unit): Data<List<EntryEntity>> {
        // TODO : Return the created kfile
//        val result = explorerRepository.openKFile(params.kfile, params.pass)
//        return Data.succes(result)
        return Data(Status.LOADING, null, null)
    }
}