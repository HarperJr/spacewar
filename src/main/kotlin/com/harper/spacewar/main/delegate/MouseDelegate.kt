package com.harper.spacewar.main.delegate

import com.harper.spacewar.display.listener.MouseListener

class MouseDelegate : ObservableDelegate<MouseListener>(), MouseListener {
    override fun onClicked(x: Float, y: Float) {
        notify { onClicked(x, y) }
    }

    override fun onPressed(x: Float, y: Float) {
        notify { onPressed(x, y) }
    }

    override fun onMoved(x: Float, y: Float) {
        notify { onMoved(x, y) }
    }

    companion object {
        private val selfInstance: MouseDelegate? = null
        val instance: MouseDelegate
            get() {
                return synchronized(this) {
                    if (selfInstance == null)
                        MouseDelegate()
                    selfInstance!!
                }
            }
    }
}
