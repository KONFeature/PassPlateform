package com.nivelais.kpass.domain.usecases.manager

import com.nivelais.kpass.domain.common.Data
import com.nivelais.kpass.domain.common.Error
import com.nivelais.kpass.domain.common.Status
import com.nivelais.kpass.domain.entities.KFileEntity
import com.nivelais.kpass.domain.exception.CreateException
import com.nivelais.kpass.domain.repository.KFilesManagerRepository
import com.nivelais.kpass.domain.usecases.UseCase

/**
 * When the user want to create a kfile
 */
class CreateKFileUseCase(private val managerRepository: KFilesManagerRepository) :
    UseCase<KFileEntity, CreateKFileUseCase.Params>() {

    override suspend fun run(params: Params): Data<KFileEntity> {
        return try {
            val result = managerRepository.createKFile(params.name, params.pass)
            Data.succes(result)
        } catch (exception: CreateException) {
            Data(Status.ERROR, null, Error(exception.message, exception))
        }
    }

    data class Params(
        val name: String,
        val pass: String
    )
}