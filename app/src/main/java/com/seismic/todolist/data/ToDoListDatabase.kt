package com.seismic.todolist.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ToDoList::class],
    version = 1,
    exportSchema = false
)
abstract class ToDoListDatabase : RoomDatabase() {
    abstract fun toDoListDao(): ToDoListDao
}