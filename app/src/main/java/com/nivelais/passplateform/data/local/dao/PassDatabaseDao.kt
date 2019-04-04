package com.nivelais.passplateform.data.local.dao

import com.nivelais.passplateform.App
import com.nivelais.passplateform.data.local.entities.PassDatabase
import com.nivelais.passplateform.data.local.entities.PassDatabase_
import io.objectbox.Box
import io.objectbox.android.ObjectBoxLiveData
import io.objectbox.kotlin.boxFor
import io.objectbox.query.QueryBuilder

object PassDatabaseDao {

    // Object store
    val store: Box<PassDatabase> = App.store.boxFor()

    // Save a passwordDatabse
    fun save(passDb: PassDatabase) = store.put(passDb)

    // Find a passwordDatabse with id
    fun findById(id: Long) = store.get(id)

    // Get last openned passwordDatabase
    fun lastOpenned(limit: Long) = store.query().order(PassDatabase_.lastOpen, QueryBuilder.DESCENDING).build().find(0, limit)

}