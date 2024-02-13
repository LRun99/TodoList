package com.example.todolist.data

import android.content.Context

interface AppContainer{
    val dataRepository: DataRepository
}

class AppDataContainer(private val context: Context) : AppContainer{
    override val dataRepository: DataRepository by lazy{
        OfflineDataRepository(InventoryDatabase.getDatabase(context).dataDao())
    }
}