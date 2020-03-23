package com.harper.spacewar.display.listener

import com.harper.spacewar.display.Key

interface KeyboardListener {
    fun onPressed(key: Key)

    fun onReleased(key: Key)
}