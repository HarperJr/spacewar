package com.harper.spacewar.main

import com.harper.spacewar.CameraSource
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
import com.harper.spacewar.main.resource.ResourceRegistry

class Spacewar : Runnable, DisplayListener {
    val textureManager: TextureManager = TextureManager()
    val fontDrawer: FontDrawer = FontDrawer(textureManager)
    val resourceRegistry: ResourceRegistry = ResourceRegistry(textureManager)

    val scaledResolution: ScaledResolution
        get() = scaledResolutionProvider.resolution

    var camera: CameraSource = Camera(this, 45f)

    /**
     * Main display
     */
    private val display = Display(DEF_DP_WIDTH, DEF_DP_HEIGHT, DP_TITLE, false, this, Mouse, Keyboard)

    private val logger = Logger.getLogger<Spacewar>()
    private val scaledResolutionProvider = ScaledResolutionProvider()
    private val timer: Timer = Timer(20f)
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
                timer.update()
                if (isRunning)
                    display.update()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            logger.error(ex)
        } finally {
            display.terminate()
        }
    }

    override fun onInitialized() {
        GlUtils.glClearColor(0xa0efefff)
        fontDrawer.drawCenteredText("Please stand by...", displayWidth / 2f, displayHeight / 2f, 0xffffffff, 1f)

        spacewarController.initialize()
    }

    override fun onUpdated() {
        GlUtils.glClear(GlUtils.COLOR_DEPTH_BUFFER_BIT)

        if (display.width != displayWidth || display.height != displayHeight)
            updateCurrentResolution(display.width, display.height)

        spacewarController.update(timer.time)
    }

    override fun onDestroyed() {
        spacewarController.destroy()
    }

    private fun updateCurrentResolution(width: Int, height: Int) {
        this.displayWidth = width
        this.displayHeight = height
        scaledResolutionProvider.resolve(this.displayWidth, this.displayHeight)
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
        private const val DEF_DP_HEIGHT = 640
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