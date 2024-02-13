package com.example.todolist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Data::class], version = 1, exportSchema = false)
abstract class InventoryDatabase : RoomDatabase() {

    abstract fun dataDao(): DataDao

    companion object {
        @Volatile
        private var Instance: InventoryDatabase? = null

        fun getDatabase(context: Context): InventoryDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, InventoryDatabase::class.java, "database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

//@Database(entities = [Data::class], version = 1, exportSchema = false)
//abstract class EventDatabase : RoomDatabase() {
//
//    abstract fun eventDataDao(): DataDao
//
//    companion object {
//        @Volatile
//        private var Instance: EventDatabase? = null
//
//        fun getDatabase(context: Context): EventDatabase {
//            return Instance ?: synchronized(this) {
//                Room.databaseBuilder(context, EventDatabase::class.java, "database")
//                    .build()
//                    .also { Instance = it }
//            }
//        }
//    }
//}
