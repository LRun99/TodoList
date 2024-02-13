package com.example.todolist.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.todolist.TodoListApplication
import com.example.todolist.ui.scenes.DataAddViewModel
import com.example.todolist.ui.scenes.DataDetailsViewModel
import com.example.todolist.ui.scenes.DataEditViewModel
import com.example.todolist.ui.scenes.HomeViewModel

object AppViewModelProvider{
    val Factory = viewModelFactory {
        initializer {
            DataAddViewModel(
                MyApplication().container.dataRepository

            )
        }
        initializer {
            DataEditViewModel(
                this.createSavedStateHandle(),
                MyApplication().container.dataRepository
            )
        }
        initializer {
            DataDetailsViewModel(
                this.createSavedStateHandle(),
                MyApplication().container.dataRepository
            )
        }
        initializer {
            HomeViewModel(
                MyApplication().container.dataRepository
            )
        }
    }
}


fun CreationExtras.MyApplication(): TodoListApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TodoListApplication)