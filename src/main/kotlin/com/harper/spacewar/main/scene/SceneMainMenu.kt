package com.harper.spacewar.main.scene

import com.harper.spacewar.main.Camera
import com.harper.spacewar.main.Spacewar
import com.harper.spacewar.main.SpacewarController
import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.entity.EntitySpaceship
import com.harper.spacewar.main.gui.GuiContainer
import com.harper.spacewar.main.gui.impl.GuiMainMenu

class SceneMainMenu(spacewar: Spacewar, private val spacewarController: SpacewarController) : Scene(spacewar) {
    override val camera: Camera = Camera(this, 56f)

    private val guiMainMenu: GuiContainer = GuiMainMenu(this, spacewar.fontRenderer, spacewar.textureManager)

    private lateinit var spaceshipEntity: Entity
    private var rotYaw: Float = 0f
    private var prevYaw: Float = 0f

    override fun createScene() {
        this.spaceshipEntity = addEntity(EntitySpaceship(this), 0f, 0f, 0f)
        this.camera.apply {
            val bounds = this@SceneMainMenu.spaceshipEntity.getBounds()
            setPosition(
                (bounds.minX + bounds.maxX) / 2f,
                (bounds.minY + bounds.maxY) / 2f + (bounds.maxY - bounds.minY) * 1.2f,
                (bounds.minZ + bounds.maxZ) / 2f - (bounds.maxZ - bounds.minZ) * 1.2f
            )
        }

        super.setGui(this.guiMainMenu)
    }

    override fun update(time: Float) {
        this.prevYaw = (this.prevYaw + (this.rotYaw - this.prevYaw) * time) % 360f
        this.spaceshipEntity.rotate(this.prevYaw, 0f)
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