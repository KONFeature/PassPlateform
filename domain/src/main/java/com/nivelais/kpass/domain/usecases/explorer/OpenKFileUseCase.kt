package com.nivelais.kpass.domain.usecases.explorer

import com.nivelais.kpass.domain.common.Data
import com.nivelais.kpass.domain.entities.KFileEntity
import com.nivelais.kpass.domain.repository.KFilesExplorerRepository
import com.nivelais.kpass.domain.usecases.UseCase

/**
 * When the user want to create a kfile
 */
class OpenKFileUseCase(private val explorerRepository: KFilesExplorerRepository) :
    UseCase<KFileEntity, OpenKFileUseCase.Params>() {

    override suspend fun run(params: Params): Data<KFileEntity> {
        // TODO : Return the created kfile
        val result = explorerRepository.openKFile(params.kfile, params.pass)
        return Data.succes(result)
    }

    data class Params(
        val kfile: KFileEntity,
        val pass: String
    )
}