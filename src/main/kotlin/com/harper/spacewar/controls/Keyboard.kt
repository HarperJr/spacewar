package com.harper.spacewar.controls

import com.conceptic.firefly.app.screen.Key
import com.harper.spacewar.display.listener.KeyboardListener

object Keyboard : KeyboardListener {
    var pressedKey: Key = Key.UNDEFINED
        private set

    override fun onPressed(key: Key) {
        this.pressedKey = key
    }

    override fun onReleased(key: Key) {
        this.pressedKey = Key.UNDEFINED
    }
}
