package com.example.todolist.data

import kotlinx.coroutines.flow.Flow

class OfflineDataRepository(private val dataDao: DataDao) : DataRepository {
    override fun getAlldataStream(): Flow<List<Data>> = dataDao.getAllData()

    override fun getDataStream(id: Int): Flow<Data?> = dataDao.getData(id)

    override fun getAllTodoDataStream(): Flow<List<Data>> = dataDao.getTodoData()

    override fun getAllEventDataStream(): Flow<List<Data>> = dataDao.getEventData()

    override fun getAllCompletedDataStream(): Flow<List<Data>> = dataDao.getCompleteData()

    override suspend fun insertData(data: Data) = dataDao.insert(data)

    override suspend fun deleteData(data: Data) = dataDao.delete(data)

    override suspend fun updateData(data: Data) = dataDao.update(data)
}