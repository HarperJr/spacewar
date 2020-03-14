package com.harper.spacewar.main.delegate

import com.harper.spacewar.display.listener.ScreenListener

class ScreenDelegate : ObservableDelegate<ScreenListener>(), ScreenListener {
    override fun onInitialized() {
        notify { onInitialized() }
    }

    override fun onUpdated() {
        notify { onUpdated() }
    }

    override fun onDestroyed() {
        notify { onDestroyed() }
    }

    companion object {
        private val selfInstance: ScreenDelegate? = null
        val instance: ScreenDelegate
            get() {
                return synchronized(this) {
                    if (selfInstance == null)
                        ScreenDelegate()
                    selfInstance!!
                }
            }
    }
}
