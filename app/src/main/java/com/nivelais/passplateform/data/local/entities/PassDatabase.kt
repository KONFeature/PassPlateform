package com.nivelais.passplateform.data.local.entities

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.*

@Entity
data class PassDatabase (
    var name: String,
    var path: String,
    var provider: String,
    var lastOpen: Date
) {
    @Id var id: Long = 0
}