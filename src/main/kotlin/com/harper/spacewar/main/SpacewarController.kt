package com.harper.spacewar.main

import com.conceptic.firefly.app.screen.Key
import com.harper.spacewar.display.listener.DisplayListener
import com.harper.spacewar.display.listener.KeyboardListener
import com.harper.spacewar.display.listener.MouseListener
import com.harper.spacewar.logging.Logger
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.font.FontDrawer
import com.harper.spacewar.main.gl.texture.TextureManager
import com.harper.spacewar.main.gui.GuiContainer
import com.harper.spacewar.main.gui.impl.GuiMainMenu
import com.harper.spacewar.main.resolution.ScaledResolution

class SpacewarController(private val spacewar: Spacewar) : DisplayListener, MouseListener, KeyboardListener {
    private val logger = Logger.getLogger<SpacewarController>()
    private val fontDrawer: FontDrawer
        get() = spacewar.fontDrawer

    private val textureManager: TextureManager
        get() = spacewar.textureManager

    private val guiContainer: GuiContainer = GuiMainMenu(this, fontDrawer, textureManager)

    private var scaledResolution: ScaledResolution = spacewar.scaledResolution
    private var isInMainMenu = true

    override fun onInitialized() {

    }

    override fun onUpdated() {
        renderOverlay()
    }

    override fun onDestroyed() {

    }

    override fun onClicked(x: Float, y: Float) {
        guiContainer.onClicked(x, scaledResolution.scaledHeight - y)
    }

    override fun onPressed(x: Float, y: Float) {
        guiContainer.onPressed(x, scaledResolution.scaledHeight - y)
    }

    override fun onMoved(x: Float, y: Float) {
        guiContainer.onMoved(x, scaledResolution.scaledHeight - y)
    }

    override fun onPressed(key: Key) {

    }

    override fun onReleased(key: Key) {

    }

    private fun renderOverlay() {
        isInMainMenu = guiContainer is GuiMainMenu

        if (scaledResolution != spacewar.scaledResolution) {
            scaledResolution = spacewar.scaledResolution
            guiContainer.onResolutionChanged(scaledResolution)
        }

        updateOverlayCamera()
        guiContainer.drawGui()
    }

    fun onEnterBtnClicked() {
        logger.info("Enter game btn is clicked")
    }

    fun onLoadBtnClicked() {
        logger.info("Load game btn is clicked")
    }

    fun onLeaveBtnClicked() {
        logger.info("Leave game btn is clicked")
    }

    private fun updateOverlayCamera() {
        val (scaledWidth, scaledHeight) = scaledResolution
        GlUtils.glMatrixMode(GlUtils.PROJECTION)
        GlUtils.glLoadIdentity()
        GlUtils.glOrtho(0.0, scaledWidth.toDouble(), scaledHeight.toDouble(), 0.0, 0.1, 1000.0)
        GlUtils.glMatrixMode(GlUtils.MODELVIEW)
        GlUtils.glLoadIdentity()
        GlUtils.glTranslatef(0f, 0f, -100f)
    }
}
