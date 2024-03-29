package com.example.todolist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data: Data)

    @Update
    suspend fun update(data: Data)

    @Delete
    suspend fun delete(data: Data)

    @Query("SELECT * from data WHERE id = :id")
    fun getData(id: Int): Flow<Data>

    @Query("SELECT * from data ORDER BY time ASC")
    fun getAllData(): Flow<List<Data>>

    @Query("SELECT * from data WHERE type == 1 ORDER BY time ASC")
    fun getTodoData(): Flow<List<Data>>

    @Query("SELECT * from data WHERE type == 2 ORDER BY time ASC")
    fun getEventData(): Flow<List<Data>>

    @Query("SELECT * from data WHERE type == 3 ORDER BY time ASC")
    fun getCompleteData(): Flow<List<Data>>
}