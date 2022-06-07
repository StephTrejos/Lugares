package com.example.lugares.adapter;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.*;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.lugares.model.Lugar;



@Dao
interface LugarDao {
    @Query("SELECT * FROM Lugar")
    fun getAllData() : LiveData<List<Lugar>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLugar(lugar: Lugar)

    @Update
    suspend fun updateLugar(lugar: Lugar)

    @Delete
    suspend fun deleteLugar(lugar: Lugar)
}
