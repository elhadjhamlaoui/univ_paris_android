package com.example.projetcomposants.BDD

import androidx.room.*

@Dao
interface MyDao {
    @Query("SELECT * FROM Flux")
    fun getallFlux(): MutableList<Flux>

    @Query("select * from Info where active = :active ")
    fun getAllInfo(active: Boolean = true): MutableList<Info>

    @Query("select * from Info WHERE nouveau = :nouveau and active = :active")
    fun getNewInfo(nouveau: Boolean = true, active: Boolean = true): MutableList<Info>


    @Query("UPDATE Info SET active = :active WHERE linkSource = :link")
    fun switchActivateInfo(link: String, active: Boolean = false)

    @Query("UPDATE Flux SET active = :active WHERE link = :link")
    fun disactivateFlux(link: String, active: Boolean = false)


    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertFlux(flux: Flux): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertInfo( info: Info): Long

    @Delete
    fun deleteFlux(vararg flux:Flux):Int
    @Delete
    fun deleteInfo(vararg info: Info):Int

    @Query("UPDATE Info SET nouveau = :nouveau WHERE nouveau != :nouveau")
    fun updateNouveau(nouveau: Boolean = false)
}