package com.wikicoding.composetodolist.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wikicoding.composetodolist.data.TodoRepository
import com.wikicoding.composetodolist.data.Todo
import com.wikicoding.composetodolist.db.DbGraph
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
class TodoViewModel(
    private val todoRepository: TodoRepository = DbGraph.todoRepository
) : ViewModel() {
    var todoDescriptionState by mutableStateOf("")
    var todoCurrentDateState by mutableStateOf(LocalDateTime.now())
    var todoDueDateState by mutableStateOf("")
    var todoCompletedState by mutableStateOf(false)

    fun onTodoDescriptionChange(newDescription: String) {
        todoDescriptionState = newDescription
    }

    fun onTodoDueDateChange(newDueDate: String) {
        todoDueDateState = newDueDate
    }

    lateinit var getAll: Flow<List<Todo>>

    init {
        viewModelScope.launch {
            getAll = todoRepository.getAll().map {todos ->
                todos.sortedByDescending { it.currentDate }
            }
        }
    }

    fun add(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.addNewTodo(todo)
        }
    }

    fun update(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.update(todo)
        }
    }

    fun delete(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.delete(todo)
        }
    }

    fun findById(id: Int): Flow<Todo> {
        return todoRepository.getById(id)
    }
}