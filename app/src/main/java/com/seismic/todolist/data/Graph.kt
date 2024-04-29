package com.seismic.todolist.data

import android.content.Context
import androidx.room.Room

object Graph {
    private lateinit var database: ToDoListDatabase

    fun provide(context: Context) {
        database = Room.databaseBuilder(context, ToDoListDatabase::class.java, "ToDoList.db").build()
    }

    val ToDoListRepository by lazy {
        ToDoListRepository(toDoListDao = database.toDoListDao())
    }
}