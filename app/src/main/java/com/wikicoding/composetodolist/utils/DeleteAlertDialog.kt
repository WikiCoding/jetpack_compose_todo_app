package com.wikicoding.composetodolist.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wikicoding.composetodolist.data.Todo
import com.wikicoding.composetodolist.viewmodel.TodoViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DeleteAlertDialog(todo: Todo, todoViewModel: TodoViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    AlertDialog(onDismissRequest = { showDialog = false },
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    todoViewModel.delete(todo)
                }) {
                    Text(text = "DELETE")
                }

                Button(onClick = { showDialog = false }) {
                    Text(text = "CANCEL")
                }
            }
        },
        title = { Text(text = "Are you sure you want to delete?") }
    )
}
