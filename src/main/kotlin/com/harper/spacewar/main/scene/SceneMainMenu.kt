package com.harper.spacewar.main.scene

import com.harper.spacewar.controls.Mouse
import com.harper.spacewar.logging.Logger
import com.harper.spacewar.main.Spacewar
import com.harper.spacewar.main.entity.SpaceshipEntity
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gui.GuiContainer
import com.harper.spacewar.main.gui.impl.GuiMainMenu
import com.harper.spacewar.main.resolution.ScaledResolution

class SceneMainMenu(spacewar: Spacewar) : Scene(spacewar) {
    private val logger = Logger.getLogger<SceneMainMenu>()
    private val spaceshipEntity: SpaceshipEntity = SpaceshipEntity(this)

    private var scaledResolution: ScaledResolution = spacewar.scaledResolution
    private var guiContainer: GuiContainer = GuiMainMenu(this, spacewar.fontDrawer, spacewar.textureManager)

    private var isInMainMenu = false

    override fun prepareScene() {

    }

    override fun onUpdated() {
        drawGui()
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

    private fun drawGui() {
        isInMainMenu = guiContainer is GuiMainMenu

        if (scaledResolution != spacewar.scaledResolution) {
            scaledResolution = spacewar.scaledResolution
            guiContainer.onResolutionChanged(scaledResolution)
        }

        val (scaledWidth, scaledHeight) = scaledResolution
        val mouseX = Mouse.xPos * scaledWidth / spacewar.displayWidth
        val mouseY = scaledHeight - Mouse.yPos * scaledHeight / spacewar.displayHeight

        guiContainer.onMoved(mouseX, mouseY)
        if (Mouse.isClicked) {
            guiContainer.onClicked(mouseX, mouseY)
        }

        GlUtils.glMatrixMode(GlUtils.PROJECTION)
        GlUtils.glLoadIdentity()
        GlUtils.glOrtho(0.0, scaledWidth.toDouble(), scaledHeight.toDouble(), 0.0, 0.1, 1000.0)
        GlUtils.glMatrixMode(GlUtils.MODELVIEW)
        GlUtils.glLoadIdentity()
        GlUtils.glTranslatef(0f, 0f, -100f)

        guiContainer.drawGui()
    }
}