package com.example.todolist.data

import kotlinx.coroutines.flow.Flow

interface DataRepository {
    fun getAlldataStream(): Flow<List<Data>>

    fun getDataStream(id: Int): Flow<Data?>

    fun getAllTodoDataStream(): Flow<List<Data>>

    fun getAllEventDataStream(): Flow<List<Data>>

    fun getAllCompletedDataStream(): Flow<List<Data>>

    suspend fun insertData(data: Data)

    suspend fun deleteData(data: Data)

    suspend fun updateData(data: Data)
}