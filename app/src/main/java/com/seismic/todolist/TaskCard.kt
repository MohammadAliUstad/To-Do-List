package com.seismic.todolist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.seismic.todolist.data.ToDoList
import com.seismic.todolist.data.ToDoListViewModel
import com.seismic.todolist.data.Tools

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(
    toDoList: ToDoList,
    toDoListViewModel: ToDoListViewModel = viewModel(),
    onDeleteClick: () -> Unit
) {
    var isStrikethrough by remember { mutableStateOf(false) }
    var showEdit by remember { mutableStateOf(false) }
    val customFontFamily = FontFamily(Font(R.font.product_sans_regular))
    var openDateChooser by remember { mutableStateOf(false) }
    var openColorChooser by remember { mutableStateOf(false) }
    var labelColor by remember { mutableStateOf("Priority") }
    val datePickerState = rememberDatePickerState()
    val confirmEnabled = remember { derivedStateOf { datePickerState.selectedDateMillis != null } }
    var mutableTask by remember {
        mutableStateOf(toDoList.task)
    }

    Card(
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { showEdit = true }
            .background(MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isStrikethrough,
                onCheckedChange = { isChecked ->
                    isStrikethrough = isChecked
                },
                modifier = Modifier,
            )
            Text(
                text = mutableTask,
                fontFamily = customFontFamily,
                fontWeight = FontWeight.Normal,
                textDecoration = if (isStrikethrough) TextDecoration.LineThrough else TextDecoration.None
            )
            IconButton(onClick = { onDeleteClick() }) {
                Icon(
                    modifier = Modifier.padding(),
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        if (showEdit) {
            AlertDialog(
                modifier = Modifier.padding(),
                onDismissRequest = { showEdit = false },
                title = {
                    Text(
                        text = "Edit Task",
                        fontFamily = customFontFamily
                    )
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = mutableTask,
                            onValueChange = {
                                mutableTask = it
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
                                text = (toDoList.date?.let { Tools.convertLongToTime(it) }).toString(),
                                fontSize = 16.sp,
                                fontFamily = customFontFamily
                            )
                            Button(onClick = { openDateChooser = true }
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
                            var priorityState: String =
                                if(toDoList.priority == 1){
                                "High Priority"
                            } else if (toDoList.priority == 2) {
                                "Medium Priority"
                            } else {
                                "Low Priority"
                            }
                            Text(
                                text = priorityState,
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
                        if (openDateChooser) {
                            DatePickerDialog(
                                onDismissRequest = { openDateChooser = false },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            openDateChooser = false
                                            var longDate: Long? = 0
                                            if (datePickerState.selectedDateMillis != null) {
                                                longDate = datePickerState.selectedDateMillis
                                            }
                                            toDoList.date = longDate
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
                                modifier = Modifier.padding(),
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
                                                toDoList.priority=1
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
                                                toDoList.priority=2
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
                                                toDoList.priority=3
                                            }
                                        ) {
                                            Text(
                                                text = "Low",
                                                fontFamily = customFontFamily
                                            )
                                        }
                                    }
                                },
                                confirmButton = {}
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
                                toDoListViewModel.updateTask(
                                    ToDoList(
                                        task= toDoList.task,
                                        date= toDoList.date,
                                        priority = toDoList.priority
                                    )
                                )
                                showEdit = false
                            },
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(
                                text ="Edit Task",
                                fontFamily = customFontFamily
                            )
                        }
                        Button(
                            onClick = {
                                showEdit = false
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