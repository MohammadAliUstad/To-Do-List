package com.seismic.todolist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ToDoList-table")
data class ToDoList(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "ToDoList-task")
    var task: String = "",
    @ColumnInfo(name = "ToDoList-date")
    var date: Long? = 0,
    @ColumnInfo(name = "ToDoList-priority")
    var priority: Int = 0
)