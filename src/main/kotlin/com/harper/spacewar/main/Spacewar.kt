package com.harper.spacewar.main

import com.harper.spacewar.controls.Keyboard
import com.harper.spacewar.controls.Mouse
import com.harper.spacewar.display.Display
import com.harper.spacewar.display.listener.DisplayListener
import com.harper.spacewar.logging.Logger
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.font.FontDrawer
import com.harper.spacewar.main.gl.texture.TextureManager
import com.harper.spacewar.main.resolution.ScaledResolution
import com.harper.spacewar.main.resolution.ScaledResolutionProvider

class Spacewar : Runnable, DisplayListener {
    val textureManager = TextureManager()
    val fontDrawer = FontDrawer(textureManager)
    val scaledResolution: ScaledResolution
        get() = scaledResoltionProvider.resolution

    /**
     * Main display
     */
    private val display = Display(DEF_DP_WIDTH, DEF_DP_HEIGHT, DP_TITLE, false, this, Mouse, Keyboard)

    private val logger = Logger.getLogger<Spacewar>()
    private val scaledResoltionProvider = ScaledResolutionProvider()
    private val spacewarController = SpacewarController(this)

    private var isRunning = false
    private var isInitializing = true

    var displayWidth: Int = 0
        private set
    var displayHeight: Int = 0
        private set

    override fun run() {
        try {
            isRunning = isInitializing
            while (isRunning) {
                if (isInitializing)
                    initialize()
                isRunning = display.isActive()

                if (isRunning)
                    display.update()
            }
        } catch (ex: Exception) {
            logger.error(ex)
        } finally {
            display.terminate()
        }
    }

    override fun onInitialized() {
        GlUtils.glClearColor(0xdfdfdfff)
        GlUtils.glEnable(GlUtils.DEPTH_TEST)
        GlUtils.glDepthFunc(GlUtils.DEPTH_ALWAYS)
        GlUtils.glEnableDepthMask()

        spacewarController.onInitialized()
    }

    override fun onUpdated() {
        GlUtils.glClear(GlUtils.COLOR_DEPTH_BUFFER_BIT)

        if (display.width != displayWidth || display.height != displayHeight)
            updateCurrentResolution(display.width, display.height)

        spacewarController.onUpdated()
    }

    override fun onDestroyed() {
        spacewarController.onDestroyed()
    }

    private fun updateCurrentResolution(width: Int, height: Int) {
        this.displayWidth = width
        this.displayHeight = height
        scaledResoltionProvider.resolve(this.displayWidth, this.displayHeight)
        GlUtils.glViewport(0, 0, this.displayWidth, this.displayHeight)
    }

    /**
     * Initialize resources and registry here
     */
    private fun initialize() {
        display.initialize()
        fontDrawer.initializeFont(DEF_FONT_PATH)

        isInitializing = false
    }

    companion object {
        private const val DEF_DP_WIDTH = 1020
        private const val DEF_DP_HEIGHT = 840
        private const val DP_TITLE = "Spacewar"

        private const val DEF_FONT_PATH = "fonts/default.png"

        fun start() {
            val spacewarInstance = Spacewar()
            val gameThread = Thread(spacewarInstance, "Game Main Thread")
                .apply { priority = Thread.MAX_PRIORITY }
            gameThread.start()
        }
    }
}