package com.nivelais.kpass.data.mapper

import com.nivelais.kpass.data.db.KFileDataEntity
import com.nivelais.kpass.domain.entities.KFileEntity

class KFileDataEntityMapper : Mapper<KFileDataEntity, KFileEntity>() {

    override fun map(from: KFileDataEntity): KFileEntity =
        KFileEntity(
            from.id,
            from.name ?: "test",
            from.path,
            from.lastOpen,
            null
        )
}