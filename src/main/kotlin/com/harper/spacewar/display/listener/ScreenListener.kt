package com.harper.spacewar.display.listener

interface ScreenListener {
    fun onInitialized()

    fun onUpdated()

    fun onDestroyed()
}