package com.nivelais.kpass.domain.usecases.manager

import com.nivelais.kpass.domain.common.Data
import com.nivelais.kpass.domain.common.Error
import com.nivelais.kpass.domain.common.Status
import com.nivelais.kpass.domain.entities.KFileEntity
import com.nivelais.kpass.domain.exception.RemoveKFileException
import com.nivelais.kpass.domain.repository.KFilesManagerRepository
import com.nivelais.kpass.domain.usecases.UseCase

/**
 * When the user want to create a kfile
 */
class RemoveKFileUseCase(private val managerRepository: KFilesManagerRepository) :
    UseCase<KFileEntity, RemoveKFileUseCase.Params>() {

    override suspend fun run(params: Params): Data<KFileEntity> {
        return try {
            managerRepository.removeKFile(params.kfile, params.removeFileToo ?: false)
            Data(Status.SUCCESSFUL, null, null)
        } catch (e: RemoveKFileException) {
            Data(Status.ERROR, null, Error(e.message, e))
        }
    }

    data class Params(
        val kfile: KFileEntity,
        val removeFileToo: Boolean?
    )
}