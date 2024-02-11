package com.wikicoding.composetodolist.uiscreens

import android.Manifest
import android.app.DatePickerDialog
import android.os.Build
import android.util.Log
import android.widget.DatePicker
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.wikicoding.composetodolist.data.Todo
import com.wikicoding.composetodolist.navigation.AppBarView
import com.wikicoding.composetodolist.navigation.Screen
import com.wikicoding.composetodolist.viewmodel.TodoViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddEditTodoScreen(navController: NavController, todoViewModel: TodoViewModel, id: Int) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val scaffoldState = rememberScaffoldState()

    if (id != 0) {
        val todo = todoViewModel.findById(id).collectAsState(
            initial = Todo(
                0,
                "",
                LocalDateTime.now(),
                LocalDateTime.now()
            )
        )

        todoViewModel.todoDescriptionState = todo.value.description
        todoViewModel.todoCurrentDateState = todo.value.currentDate
        todoViewModel.todoDueDateState = todo.value.dueDate.format(DateTimeFormatter.ISO_DATE)
    } else {
        todoViewModel.todoDescriptionState = ""
        todoViewModel.todoDueDateState = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBarView(
                title =
                if (id != 0) "Update"
                else "Add"
            )
            { navController.navigateUp() }
            /** meaning up the stack, so go back to the prev screen **/
        },
        backgroundColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            OutlinedTextField(
                value = todoViewModel.todoDescriptionState,
                onValueChange = {
                    todoViewModel.onTodoDescriptionChange(it)
                },
                label = { Text(text = "Description") },
                singleLine = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            Text(
                text = "Due date: " + todoViewModel.todoDueDateState,
                modifier = Modifier.clickable {
                    showDialog = true
                },
                fontSize = 24.sp
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = {
                    if (id == 0) {
                        todoViewModel.add(
                            todo = Todo(
                                id = 0,
                                description = todoViewModel.todoDescriptionState,
                                currentDate = LocalDateTime.now(),
                                dueDate = LocalDateTime.parse(
                                    todoViewModel.todoDueDateState + "T00:00:00.000000",
                                    DateTimeFormatter.ISO_DATE_TIME
                                )
                            )
                        )
                    } else {
                        todoViewModel.update(
                            todo = Todo(
                                id = id,
                                description = todoViewModel.todoDescriptionState,
                                currentDate = LocalDateTime.now(),
                                dueDate = LocalDateTime.parse(
                                    todoViewModel.todoDueDateState + "T00:00:00.000000",
                                    DateTimeFormatter.ISO_DATE_TIME
                                )
                            )
                        )
                    }
                    scope.launch {
                        navController.navigateUp()
                    }
                }) {
                Text(text = if (id == 0) "Add Todo" else "Update Todo")
            }

            Text(
                text = "Developed by Tiago Castro @ https://github.com/WikiCoding",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.End),
                textAlign = TextAlign.Center
            )

            if (showDialog) {
                showDialog = false
                val year: Int
                val month: Int
                val day: Int

                val calendar = Calendar.getInstance()
                year = calendar.get(Calendar.YEAR)
                month = calendar.get(Calendar.MONTH)
                day = calendar.get(Calendar.DAY_OF_MONTH)
                calendar.time = Date()

                val datePickerDialog =
                    DatePickerDialog(
                        context,
                        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                            var dayDate = dayOfMonth.toString()
                            var monthDate = month.toString()
                            if (month < 10) monthDate = "0${month + 1}"
                            if (dayOfMonth < 10) dayDate = "0${dayOfMonth}"

                            todoViewModel.todoDueDateState = "$year-$monthDate-$dayDate"
                        },
                        year,
                        month,
                        day
                    )

                datePickerDialog.show()
            }
        }
    }
}