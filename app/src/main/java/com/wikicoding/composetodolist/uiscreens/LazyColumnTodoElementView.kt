package com.wikicoding.composetodolist.uiscreens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wikicoding.composetodolist.data.Todo
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LazyColumnTodoElement(todo: Todo, onClick: () -> Unit) {
    var updatedState by remember {
        mutableStateOf(todo.completed)
    }

    Card(
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                todo.completed = !todo.completed
                updatedState = todo.completed
                onClick()
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = todo.description,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    softWrap = true,
                    overflow = TextOverflow.Visible,
                    style = if (updatedState) TextStyle(textDecoration = TextDecoration.LineThrough)
                    else TextStyle(textDecoration = TextDecoration.None)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Created: " + todo.currentDate.format(
                        DateTimeFormatter.ofPattern(
                            "dd-MM-yyyy H:MM",
                            Locale.getDefault()
                        )
                    ), fontSize = 14.sp
                )

                Text(
                    text = "Due: " + todo.dueDate.format(DateTimeFormatter.ISO_DATE),
                    fontSize = 14.sp,
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                )
            }
        }
    }
}