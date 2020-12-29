package com.example.projetcomposants.BDD

import androidx.room.*
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

@Entity(
    indices = (arrayOf(Index(value = ["link"], unique = true))), foreignKeys = (arrayOf(
        ForeignKey(
            entity = Flux::class,
            parentColumns = arrayOf("link"),
            childColumns = arrayOf("linkSource"),
            onDelete = ForeignKey.CASCADE,
            deferred = true
        )
    ))
)
data class Info
    (
    val title: String,
    val description: String,
    val link: String,
    val dateTime: String,
    val nouveau: Boolean = true,
    val linkSource: String,
    val active: Boolean = true

) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    fun getDate():java.util.Date{
        val pattern = "EEE, dd MMM yyyy HH:mm:ss Z"
        val df =  SimpleDateFormat(pattern)
        return df.parse(dateTime)
    }

}