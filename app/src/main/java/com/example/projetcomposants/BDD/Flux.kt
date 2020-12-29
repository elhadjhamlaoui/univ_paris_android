package com.example.projetcomposants.BDD

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = (arrayOf(Index(value = ["link"], unique = true))))
data class Flux(
    val source: String, val tag: String, val link: String,
    val active: Boolean = true


) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}