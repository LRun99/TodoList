package com.example.todolist.data

import kotlinx.coroutines.flow.Flow

interface DataRepository {
    fun getAlldataStream(): Flow<List<Data>>

    fun getDataStream(id: Int): Flow<Data?>

    suspend fun insertData(data: Data)

    suspend fun deleteData(data: Data)

    suspend fun updateData(data: Data)
}