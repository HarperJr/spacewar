package com.harper.spacewar.main.delegate

open class ObservableDelegate<T> {
    private val subscribers: MutableList<T> = mutableListOf()

    fun subscribe(subscriber: T) {
        subscribers.add(subscriber)
    }

    fun unsubscribe(subscriber: T) {
        subscribers.remove(subscriber)
    }

    fun notify(action: T.() -> Unit) {
        subscribers.forEach(action)
    }
}
