package com.wikicoding.composetodolist.data

import kotlinx.coroutines.flow.Flow

class TodoRepository(private val todoDao: TodoDao) {

    suspend fun addNewTodo(todo: Todo) {
        todoDao.addTodo(todo)
    }

    fun getAll(): Flow<List<Todo>> {
        return todoDao.getAllTodos()
    }

    fun getById(id: Int): Flow<Todo> {
        return todoDao.getTodoById(id)
    }

    suspend fun delete(todo: Todo) {
        todoDao.deleteTodo(todo)
    }

    suspend fun update(todo: Todo) {
        todoDao.updateTodo(todo)
    }
}