package com.seismic.todolist.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ToDoListViewModel(
    private val toDoListRepository: ToDoListRepository = Graph.ToDoListRepository
) : ViewModel() {

    var taskState by mutableStateOf("")
    var dateState by mutableLongStateOf(0)
    var priorityState by mutableIntStateOf(0)

    fun changeTask(currentTask: String) {
        taskState = currentTask
    }

    fun changeDate(currentDate: Long?) {
        if (currentDate != null) {
            dateState = currentDate
        }
    }

    fun changePriority(currentPriority: Int) {
        priorityState = currentPriority
    }

    fun addTask(toDoList: ToDoList) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoListRepository.addTask(toDoList = toDoList)
        }
    }

    fun getTask(id: Int): Flow<ToDoList> {
        return toDoListRepository.getTask(id)
    }

    lateinit var getAllTasks: Flow<List<ToDoList>>

    init {
        viewModelScope.launch {
            getAllTasks = toDoListRepository.getAllTasks()
        }
    }

    fun updateTask(toDoList: ToDoList) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoListRepository.updateTask(toDoList = toDoList)
        }
    }

    fun deleteTask(toDoList: ToDoList) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoListRepository.deleteTask(toDoList = toDoList)
        }
    }

    lateinit var getByPriority: Flow<List<ToDoList>>

    init {
        viewModelScope.launch {
            getByPriority = toDoListRepository.getByPriority()
        }
    }

    lateinit var getByDate: Flow<List<ToDoList>>

    init {
        viewModelScope.launch {
            getByDate = toDoListRepository.getByDate()
        }
    }
}