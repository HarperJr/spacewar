package com.harper.spacewar.display.listener

import com.conceptic.firefly.app.screen.Key

interface KeyboardListener {
    fun onPressed(key: Key)

    fun onReleased(key: Key)
}