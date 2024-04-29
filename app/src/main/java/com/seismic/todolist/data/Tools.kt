package com.seismic.todolist.data

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

class Tools {
    companion object {
        @SuppressLint("SimpleDateFormat")
        fun convertLongToTime(time:Long) : String {
            val date = Date(time)
            val format = SimpleDateFormat("dd MMM yyyy")
            return format.format(date)
        }
    }
}