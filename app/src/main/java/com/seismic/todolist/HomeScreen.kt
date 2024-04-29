package com.seismic.todolist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seismic.todolist.data.ToDoList
import com.seismic.todolist.data.ToDoListViewModel
import com.seismic.todolist.data.Tools

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    toDoListViewModel: ToDoListViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    val customFontFamily = FontFamily(Font(R.font.product_sans_regular))
    var expanded by remember { mutableStateOf(false) }
    var sortby by remember { mutableStateOf("By Default") }
    var dateResult by remember { mutableStateOf("Date Picker") }
    var openColorChooser by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val confirmEnabled = remember { derivedStateOf { datePickerState.selectedDateMillis != null } }
    var labelDate by remember { mutableStateOf("dd/mm/yy") }
    var labelColor by remember { mutableStateOf("Priority") }
    var openDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                title = {
                    Text(
                        text = "Tasks",
                        fontFamily = customFontFamily,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                actions = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.padding()
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "By Date",
                                    fontFamily = customFontFamily
                                )
                            },
                            onClick = {
                                sortby = "By Date"
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "By Priority",
                                    fontFamily = customFontFamily
                                )
                            },
                            onClick = {
                                sortby = "By Priority"
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "By Default",
                                    fontFamily = customFontFamily
                                )
                            },
                            onClick = {
                                sortby = "By Default"
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog = true
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }
            )
        }
    ) { it ->
        if (sortby == "By Priority") {
            val sortedByPriority by toDoListViewModel.getByPriority.collectAsState(initial = emptyList())
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                items(
                    sortedByPriority,
                    key = { task -> task.id }
                ) { task ->
                    TaskCard(
                        task,
                        onDeleteClick = {
                            toDoListViewModel.deleteTask(task)
                        }
                    )
                }
            }
        } else if (sortby == "By Date") {
            val sortedByDate by toDoListViewModel.getByDate.collectAsState(initial = emptyList())
            if (sortedByDate.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 400.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Add some Tasks",
                        fontFamily = customFontFamily
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    items(
                        sortedByDate,
                        key = { task -> task.id }
                    ) { task ->
                        TaskCard(
                            task,
                            onDeleteClick = {
                                toDoListViewModel.deleteTask(task)
                            }
                        )
                    }
                }
            }
        } else if (sortby == "By Default") {
            val taskList by toDoListViewModel.getAllTasks.collectAsState(initial = emptyList())
            if (taskList.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 400.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Add some Tasks",
                        fontFamily = customFontFamily
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    items(
                        taskList,
                        key = { task -> task.id }
                    ) { task ->
                        TaskCard(
                            task,
                            onDeleteClick = {
                                toDoListViewModel.deleteTask(task)
                            }
                        )
                    }
                }
            }
        }
        if (showDialog) {
            AlertDialog(
                modifier = Modifier.padding(it),
                onDismissRequest = { showDialog = false },
                title = {
                    Text(
                        text = "Add Task",
                        fontFamily = customFontFamily
                    )
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = toDoListViewModel.taskState,
                            onValueChange = {
                                toDoListViewModel.changeTask(it)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            label = {
                                Text(
                                    text = "Task To-Do",
                                    fontFamily = customFontFamily
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = labelDate,
                                fontSize = 16.sp,
                                fontFamily = customFontFamily
                            )
                            Button(onClick = { openDialog = true }
                            ) {
                                Text(
                                    text = "Select Date",
                                    fontFamily = customFontFamily
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = labelColor,
                                fontSize = 16.sp,
                                fontFamily = customFontFamily
                            )
                            Button(onClick = { openColorChooser = true }
                            ) {
                                Text(
                                    text = "Select Priority",
                                    fontFamily = customFontFamily
                                )
                            }
                        }
                        if (openDialog) {
                            DatePickerDialog(
                                onDismissRequest = { openDialog = false },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            openDialog = false
                                            var date = ""
                                            var longDate: Long? = 0
                                            if (datePickerState.selectedDateMillis != null) {
                                                longDate = datePickerState.selectedDateMillis
                                                date =
                                                    Tools.convertLongToTime(datePickerState.selectedDateMillis!!)
                                            }
                                            dateResult = date
                                            labelDate = dateResult
                                            toDoListViewModel.changeDate(longDate)
                                        },
                                        enabled = confirmEnabled.value
                                    ) {
                                        Text(
                                            text = "Confirm",
                                            fontFamily = customFontFamily
                                        )
                                    }
                                }
                            ) {
                                DatePicker(state = datePickerState)
                            }
                        }
                        if (openColorChooser) {
                            AlertDialog(
                                modifier = Modifier.padding(it),
                                onDismissRequest = { openColorChooser = false },
                                title = {
                                    Text(
                                        text = "Choose Priority",
                                        fontFamily = customFontFamily
                                    )
                                },
                                text = {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_circle_24),
                                            tint = Color.Red,
                                            contentDescription = null
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Button(
                                            onClick = {
                                                openColorChooser = false
                                                labelColor = "High Priority"
                                                toDoListViewModel.changePriority(1)
                                            }
                                        ) {
                                            Text(
                                                text = "High",
                                                fontFamily = customFontFamily
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_circle_24),
                                            tint = Color.Blue,
                                            contentDescription = null
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Button(
                                            onClick = {
                                                openColorChooser = false
                                                labelColor = "Medium Priority"
                                                toDoListViewModel.changePriority(2)
                                            }
                                        ) {
                                            Text(
                                                text = "Medium",
                                                fontFamily = customFontFamily
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Icon(
                                            painter = painterResource(
                                                id = R.drawable.baseline_circle_24
                                            ),
                                            tint = Color.White,
                                            contentDescription = null
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Button(
                                            onClick = {
                                                openColorChooser = false
                                                labelColor = "Low Priority"
                                                toDoListViewModel.changePriority(3)
                                            }
                                        ) {
                                            Text(
                                                text = "Low",
                                                fontFamily = customFontFamily
                                            )
                                        }
                                    }
                                },
                                confirmButton = {

                                }
                            )
                        }
                    }
                },
                confirmButton = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                toDoListViewModel.addTask(
                                    ToDoList(
                                        task = toDoListViewModel.taskState,
                                        date = toDoListViewModel.dateState,
                                        priority = toDoListViewModel.priorityState
                                    )
                                )
                                toDoListViewModel.taskState = ""
                                labelDate = "dd/mm/yy"
                                labelColor = "Priority"
                                showDialog = false
                            },
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(
                                text = "Add Task",
                                fontFamily = customFontFamily
                            )
                        }
                        Button(
                            onClick = {
                                toDoListViewModel.taskState = ""
                                labelDate = "dd/mm/yy"
                                labelColor = "Priority"
                                showDialog = false
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(
                                text = "Cancel",
                                fontFamily = customFontFamily
                            )
                        }
                    }
                }
            )
        }
    }
}