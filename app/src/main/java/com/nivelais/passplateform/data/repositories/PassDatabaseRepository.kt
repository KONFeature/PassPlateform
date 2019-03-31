package com.nivelais.passplateform.data.repositories

import com.nivelais.passplateform.data.local.dao.PassDatabaseDao

object PassDatabaseRepository {

    private val RECENTLY_OPENNED_LIMIT: Long = 5

    fun getRecentlyOpenned() = PassDatabaseDao.lastOpenned(RECENTLY_OPENNED_LIMIT)

}