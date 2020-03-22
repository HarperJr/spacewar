package com.harper.spacewar.main.resource

import java.util.concurrent.Executors
import java.util.concurrent.FutureTask

abstract class Resource<T>(protected val path: String) {
    private var loadingTask: FutureTask<T>? = null

    protected abstract fun asyncLoad(): FutureTask<T>
    private val executor = Executors.newSingleThreadExecutor()

    fun load() {
        if (this.loadingTask != null) return
        this.loadingTask = this.asyncLoad()
        executor.execute(this.loadingTask)
    }

    fun get(): T {
        if (isLoaded()) {
            return this.loadingTask!!.get()
        } else throw IllegalStateException("Please make sure that the task is currently done")
    }

    fun isLoaded(): Boolean {
        return if (this.loadingTask != null) {
            val isLoaded = this.loadingTask!!.isDone
            if (isLoaded)
                executor.shutdown()
            isLoaded
        } else false
    }
}