package com.seismic.todolist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ToDoListDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addTask(toDoList: ToDoList)

    @Query("Select * from `ToDoList-table` where id=:id")
    abstract fun getTask(id: Int): Flow<ToDoList>

    @Query("Select * from `ToDoList-table`")
    abstract fun getAllTasks(): Flow<List<ToDoList>>

    @Update
    abstract suspend fun updateTask(toDoList: ToDoList)

    @Delete
    abstract suspend fun deleteTask(toDoList: ToDoList)

    @Query("SELECT * from `ToDoList-table` ORDER BY `ToDoList-priority` ASC")
    abstract fun getByPriority(): Flow<List<ToDoList>>

    @Query("SELECT * from `ToDoList-table` ORDER BY `ToDoList-date` ASC")
    abstract fun getByDate(): Flow<List<ToDoList>>
}