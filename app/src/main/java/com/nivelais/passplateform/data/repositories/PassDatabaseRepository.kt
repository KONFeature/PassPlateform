package com.nivelais.passplateform.data.repositories

import com.nivelais.passplateform.data.local.dao.PassDatabaseDao
import com.nivelais.passplateform.data.local.entities.PassDatabase

object PassDatabaseRepository {

    private val RECENTLY_OPENNED_LIMIT: Long = 5

    fun getRecentlyOpenned(): MutableList<PassDatabase> =
        PassDatabaseDao.lastOpenned(RECENTLY_OPENNED_LIMIT)

    fun save(db: PassDatabase) = PassDatabaseDao.save(db)

    fun findById(id: Long) = PassDatabaseDao.findById(id)

    fun findByName(name: String) = PassDatabaseDao.findByName(name)
}