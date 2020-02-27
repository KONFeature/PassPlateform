package com.nivelais.kpass.domain.usecases.manager

import com.nivelais.kpass.domain.common.Data
import com.nivelais.kpass.domain.common.Status
import com.nivelais.kpass.domain.entities.KFileEntity
import com.nivelais.kpass.domain.repository.KFilesManagerRepository
import com.nivelais.kpass.domain.usecases.UseCase

/**
 * List all the kfiles known
 */
class ListKFilesUseCase(private val managerRepository: KFilesManagerRepository) :
    UseCase<List<KFileEntity>, Unit>() {

    override suspend fun run(params: Unit): Data<List<KFileEntity>> {
        // TODO : Do Something
        return Data(Status.SUCCESSFUL, managerRepository.listKFiles(), null)
    }
}