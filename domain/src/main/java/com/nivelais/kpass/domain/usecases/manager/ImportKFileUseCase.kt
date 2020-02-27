package com.nivelais.kpass.domain.usecases.manager

import com.nivelais.kpass.domain.common.Data
import com.nivelais.kpass.domain.common.Status
import com.nivelais.kpass.domain.entities.KFileEntity
import com.nivelais.kpass.domain.repository.KFilesManagerRepository
import com.nivelais.kpass.domain.usecases.UseCase

/**
 * When the user want to create a kfile
 */
class ImportKFileUseCase(private val managerRepository: KFilesManagerRepository) :
    UseCase<KFileEntity, ImportKFileUseCase.Params>() {

    override suspend fun run(params: Params): Data<KFileEntity> {
        // TODO : Do Something
        return Data(Status.LOADING, null, null)
    }

    data class Params(
        val name: String?
    )
}