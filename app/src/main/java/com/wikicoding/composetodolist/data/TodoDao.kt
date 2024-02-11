package com.wikicoding.composetodolist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TodoDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addTodo(todo: Todo)

    @Query("SELECT * FROM `todo_table`")
    abstract fun getAllTodos(): Flow<List<Todo>>

    @Query("SELECT * FROM `todo_table` WHERE id = :id")
    abstract fun getTodoById(id: Int): Flow<Todo>

    @Transaction
    @Update
    abstract suspend fun updateTodo(todo: Todo)

    @Transaction
    @Delete
    abstract suspend fun deleteTodo(todo: Todo)
}