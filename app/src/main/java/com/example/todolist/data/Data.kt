package com.example.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data")
data class Data(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val content: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val time: Int,
)