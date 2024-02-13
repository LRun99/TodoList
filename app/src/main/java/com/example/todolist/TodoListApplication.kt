package com.example.todolist

import android.app.Application
import com.example.todolist.data.AppContainer
import com.example.todolist.data.AppDataContainer

class TodoListApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate(){
        super.onCreate()
        container = AppDataContainer(this)
    }
}