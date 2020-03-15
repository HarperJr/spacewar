package com.harper.spacewar.display.listener

interface DisplayListener {
    fun onInitialized()

    fun onUpdated()

    fun onDestroyed()
}