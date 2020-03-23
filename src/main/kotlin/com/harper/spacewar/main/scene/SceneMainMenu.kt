package com.harper.spacewar.main.scene

import com.harper.spacewar.main.Spacewar
import com.harper.spacewar.main.SpacewarController
import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.entity.SpaceshipEntity
import com.harper.spacewar.main.gui.GuiContainer
import com.harper.spacewar.main.gui.impl.GuiMainMenu

class SceneMainMenu(spacewar: Spacewar, private val spacewarController: SpacewarController) : Scene(spacewar) {
    private val guiMainMenu: GuiContainer = GuiMainMenu(this, spacewar.fontRenderer, spacewar.textureManager)
    private lateinit var spaceshipEntity: Entity
    private var rotYaw: Float = 0f
    private var prevYaw: Float = 0f

    override fun createScene() {
        this.spacewar.camera.apply {
            setPosition(0f, 10f, -90f)
            setRotation(0f, 10f)
        }

        this.spaceshipEntity = addEntity(SpaceshipEntity(renderManager), 0f, 0f, 0f)
            .also { it.rotate(90f, 0f) }
        super.setGui(this.guiMainMenu)
    }

    override fun update(time: Float) {
        this.prevYaw = (this.prevYaw + (this.rotYaw - this.prevYaw) * time) % 360f
        this.spaceshipEntity.rotate(0f, this.prevYaw)
        this.rotYaw += 0.1f

        super.update(time)
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