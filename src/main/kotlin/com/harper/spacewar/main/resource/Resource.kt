package com.harper.spacewar.main.resource

import java.util.concurrent.FutureTask

abstract class Resource<T>(protected val path: String) {
    private var loadingTask: FutureTask<T>? = null

    protected abstract fun asyncLoad(): FutureTask<T>

    fun load() {
        if (this.loadingTask != null) return
        this.loadingTask = this.asyncLoad()
            .also { it.run() }
    }

    fun get(): T {
        if (isLoaded()) {
            return this.loadingTask!!.get()
        } else throw IllegalStateException("Please make sure that the task is currently done")
    }

    fun isLoaded(): Boolean {
        return if (this.loadingTask != null) {
            this.loadingTask!!.isDone
        } else false
    }
}