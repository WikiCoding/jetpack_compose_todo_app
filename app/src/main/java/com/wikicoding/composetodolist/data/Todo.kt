package com.wikicoding.composetodolist.data

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "todo_table")
/**improvement on LazyColumn swipe lag. @Stable is also possible to use.**/
@Immutable
data class Todo(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var description: String,
    var currentDate: LocalDateTime,
    var dueDate: LocalDateTime,
    var completed: Boolean = false
)
