package com.harper.spacewar.main.scene

import com.harper.spacewar.main.Spacewar
import com.harper.spacewar.main.SpacewarController
import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.entity.SpaceshipEntity
import com.harper.spacewar.main.gui.GuiContainer
import com.harper.spacewar.main.gui.impl.GuiMainMenu

class SceneMainMenu(spacewar: Spacewar, private val spacewarController: SpacewarController) : Scene(spacewar) {
    private val guiMainMenu: GuiContainer = GuiMainMenu(this, spacewar.fontDrawer, spacewar.textureManager)
    private lateinit var spaceshipEntity: Entity
    private var rotYaw: Float = 0f
    private var prevYaw: Float = 0f

    override fun createScene() {
        this.spaceshipEntity = addEntity(SpaceshipEntity(renderManager), -20f, -15f, -100f)
            .also { it.rotate(90f, 60f) }
        this.guiContainer = this.guiMainMenu
    }

    override fun update(time: Float) {
        this.prevYaw = (this.prevYaw + (this.rotYaw - this.prevYaw) * time) % 360f
        this.spaceshipEntity.rotate(0f, this.prevYaw)
        super.update(time)
        this.rotYaw += 0.00005f
    }

    fun onEnterBtnClicked() {
        spacewarController.loadInGameScene()
    }

    fun onLoadBtnClicked() {
        logger.info("Load game btn is clicked")
    }

    fun onLeaveBtnClicked() {
        logger.info("Leave game btn is clicked")
    }
}