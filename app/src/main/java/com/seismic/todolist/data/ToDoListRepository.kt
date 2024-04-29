package com.seismic.todolist.data

import kotlinx.coroutines.flow.Flow

class ToDoListRepository(private val toDoListDao: ToDoListDao) {

    suspend fun addTask(toDoList: ToDoList){
        toDoListDao.addTask(toDoList)
    }

    fun getTask(id: Int): Flow<ToDoList> {
        return toDoListDao.getTask(id)
    }

    fun getAllTasks(): Flow<List<ToDoList>> {
        return toDoListDao.getAllTasks()
    }

    suspend fun updateTask(toDoList: ToDoList){
        toDoListDao.updateTask(toDoList)
    }

    suspend fun deleteTask(toDoList: ToDoList){
        toDoListDao.deleteTask(toDoList)
    }

    fun getByPriority() : Flow<List<ToDoList>> {
        return toDoListDao.getByPriority()
    }

    fun getByDate() : Flow<List<ToDoList>> {
        return toDoListDao.getByDate()
    }
}