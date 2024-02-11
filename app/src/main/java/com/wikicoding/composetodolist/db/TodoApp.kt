package com.wikicoding.composetodolist.db

import android.app.Application

class TodoApp: Application() {
    override fun onCreate() {
        super.onCreate()
        DbGraph.provide(this)
    }
}