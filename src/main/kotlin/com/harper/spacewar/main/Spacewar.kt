package com.harper.spacewar.main

import com.harper.spacewar.display.Display
import com.harper.spacewar.logging.Logger
import com.harper.spacewar.main.delegate.KeyboardDelegate
import com.harper.spacewar.main.delegate.MouseDelegate
import com.harper.spacewar.main.delegate.ScreenDelegate
import com.harper.spacewar.main.gl.GlHandler

class Spacewar : Runnable {
    private val logger = Logger.getLogger<Spacewar>()

    /**
     * Main display
     */
    private val display = Display(
        DEF_DP_WIDTH,
        DEF_DP_HEIGHT,
        DP_TITLE,
        false,
        ScreenDelegate.instance,
        MouseDelegate.instance,
        KeyboardDelegate.instance
    )

    private var isRunning = false
    private var isInitializing = true

    override fun run() {
        try {
            isRunning = isInitializing
            while (isRunning) {
                if (isInitializing)
                    initialize()
                isRunning = display.isActive()

                display.update()
            }
        } catch (ex: RuntimeException) {
            logger.error(ex)
        } finally {
            display.terminate()
        }
    }

    private fun initialize() {
        display.initialize()
        GlHandler.clearColor(0xffffffff)

        isInitializing = false
    }

    companion object {
        private const val DEF_DP_WIDTH = 1020
        private const val DEF_DP_HEIGHT = 840
        private const val DP_TITLE = "Spacewar"

        fun start() {
            val spacewarInstance = Spacewar()
            val gameThread = Thread(spacewarInstance, "Game Main Thread")
                .apply { priority = Thread.MAX_PRIORITY }
            gameThread.start()
        }
    }
}