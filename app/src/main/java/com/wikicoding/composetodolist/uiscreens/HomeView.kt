package com.wikicoding.composetodolist.uiscreens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Scaffold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberDismissState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.wikicoding.composetodolist.data.Todo
import com.wikicoding.composetodolist.navigation.AppBarView
import com.wikicoding.composetodolist.navigation.Screen
import com.wikicoding.composetodolist.viewmodel.TodoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController) {
    val todoViewModel = viewModel<TodoViewModel>()

    val todoList = todoViewModel.getAll.collectAsState(initial = listOf())

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBarView(title = "Todo List")
        },
        modifier = Modifier.padding(8.dp),
        backgroundColor = MaterialTheme.colorScheme.background
    ) {
        var showDialog by remember { mutableStateOf(false) }
        var todoToDelete: Todo? by remember { mutableStateOf(null) }

        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(0.8f)) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    items(items = todoList.value, key = { todo -> todo.id }) { todo ->
                        val dismissState = rememberDismissState(
                            confirmStateChange = {
                                if (it == DismissValue.DismissedToEnd) {
                                    showDialog = true
                                    todoToDelete = todo
                                }
                                if (it == DismissValue.DismissedToStart) {
                                    val id = todo.id
                                    navController.navigate(Screen.AddEditScreen.route + "/$id")
                                }
                                true
                            }
                        )

                        SwipeToDismiss(
                            state = dismissState,
                            background = {
                                val color by animateColorAsState(
                                    if (dismissState.dismissDirection == DismissDirection.StartToEnd) Color.Red
                                    else if (dismissState.dismissDirection == DismissDirection.EndToStart) Color.Green
                                    else Color.Transparent,
                                    label = ""
                                )

                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .background(color)
                                        .padding(horizontal = 20.dp),
                                ) {
                                    if (dismissState.dismissDirection == DismissDirection.StartToEnd) {
                                        Icon(
                                            Icons.Default.Delete, contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.align(Alignment.CenterStart)
                                        )
                                    } else {
                                        Icon(
                                            Icons.Default.Edit, contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.align(Alignment.CenterEnd)
                                        )
                                    }
                                }
                            },
                            directions = setOf(
                                DismissDirection.StartToEnd,
                                DismissDirection.EndToStart
                            ),
                            dismissThresholds = { FractionalThreshold(0.25f) },
                            dismissContent = {
                                LazyColumnTodoElement(todo = todo) {
                                    todoViewModel.update(todo = todo)
                                }
                            }
                        )

                        /** reset the dismissState after consuming the updateStateChange and
                         * reset the dismissState of the Delete swipe after cancel **/
                        if (dismissState.isDismissed(DismissDirection.EndToStart) || !showDialog) {
                            LaunchedEffect(key1 = todo) {
                                dismissState.reset()
                                dismissState.reset()
                                /** double reset solves the background color for some reason **/
                            }
                        }
                    }
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = {
                    navController.navigate(Screen.AddEditScreen.route + "/0")
                }) {
                Text(text = "Add Todo")
            }
            Text(
                text = "Developed by Tiago Castro @ https://github.com/WikiCoding",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.End),
                textAlign = TextAlign.Center
            )

        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                    todoToDelete = null
                },
                confirmButton = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = {
                            todoToDelete?.let {
                                todoViewModel.delete(it)
                            }
                            showDialog = false
                            todoToDelete = null
                        }) {
                            Text(text = "DELETE")
                        }

                        Button(onClick = {
                            showDialog = false
                            todoToDelete = null
                            navController.navigateUp()
                        }) {
                            Text(text = "CANCEL")
                        }
                    }
                },
                title = { Text(text = "Are you sure you want to delete?") }
            )
        }
    }
}