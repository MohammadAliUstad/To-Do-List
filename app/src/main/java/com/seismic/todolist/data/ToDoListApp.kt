package com.seismic.todolist.data

import android.app.Application

class ToDoListApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}