package com.harper.spacewar.main.delegate

import com.conceptic.firefly.app.screen.Key
import com.harper.spacewar.display.listener.KeysListener

class KeyboardDelegate : ObservableDelegate<KeysListener>(), KeysListener {
    override fun onPressed(key: Key) {
        notify { onPressed(key) }
    }

    override fun onReleased(key: Key) {
        notify { onReleased(key) }
    }

    companion object {
        private val selfInstance: KeyboardDelegate? = null
        val instance: KeyboardDelegate
            get() {
                return synchronized(this) {
                    if (selfInstance == null)
                        MouseDelegate()
                    selfInstance!!
                }
            }
    }
}
