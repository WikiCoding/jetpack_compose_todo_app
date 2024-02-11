package com.wikicoding.composetodolist.db

import android.content.Context
import androidx.room.Room
import com.wikicoding.composetodolist.data.TodoDatabase
import com.wikicoding.composetodolist.data.TodoRepository

object DbGraph {
    private lateinit var todoDatabase: TodoDatabase

    val todoRepository by lazy {
        TodoRepository(todoDatabase.todoDao())
    }

    fun provide(context: Context) {
        todoDatabase = Room.databaseBuilder(
            context,
            TodoDatabase::class.java,
            "compose_todo_database.db"
        )
//            .fallbackToDestructiveMigration()
            .build()
    }
}