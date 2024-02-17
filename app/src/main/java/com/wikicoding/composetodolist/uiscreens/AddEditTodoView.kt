package com.wikicoding.composetodolist.uiscreens

import android.app.DatePickerDialog
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wikicoding.composespeechtotext.VoiceToTextParser
import com.wikicoding.composetodolist.data.Todo
import com.wikicoding.composetodolist.navigation.AppBarView
import com.wikicoding.composetodolist.viewmodel.TodoViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddEditTodoScreen(navController: NavController, id: Int, voiceToTextParser: VoiceToTextParser) {
    val speechTextState by voiceToTextParser.state.collectAsState()

    val todoViewModel = viewModel<TodoViewModel>()
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

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
        todoViewModel.todoDescriptionState = speechTextState.spokenText
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
            {
                navController.navigateUp()
            }
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

            LaunchedEffect(key1 = speechTextState.spokenText) {
                todoViewModel.todoDescriptionState = speechTextState.spokenText
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 36.dp)
            )

            Text(
                text = "Due date: " + todoViewModel.todoDueDateState,
                modifier = Modifier.clickable {
                    showDialog = true
                },
                fontSize = 24.sp
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 36.dp)
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(64.dp),
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

                    navController.navigateUp()
                }) {
                Text(fontSize = 18.sp, text = if (id == 0) "Add Todo" else "Update Todo")
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 36.dp)
            )

            if (id == 0) {
                Button(onClick = {
                    if (speechTextState.isSpeaking) {
                        voiceToTextParser.stopListening()
                    } else {
                        voiceToTextParser.startListening("PT")
                    }
                }, modifier = Modifier.padding(8.dp)) {
                    AnimatedContent(
                        targetState = speechTextState.isSpeaking,
                        label = ""
                    ) { isSpeaking ->
                        if (isSpeaking) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Stop,
                                    contentDescription = null
                                )
                                Row {
                                    Text(fontSize = 18.sp, text = "Listening...")
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Mic,
                                    contentDescription = null
                                )
                                Row {
                                    Text(fontSize = 18.sp, text = "Click to start speech to text")
                                }
                            }
                        }
                    }
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 36.dp)
                )
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