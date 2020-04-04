package com.harper.spacewar.main.scene

import com.harper.spacewar.main.Spacewar
import com.harper.spacewar.main.SpacewarController
import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.entity.impl.EntitySpaceshipStatic
import com.harper.spacewar.main.gui.GuiContainer
import com.harper.spacewar.main.gui.impl.GuiMainMenu

class SceneMainMenu(spacewar: Spacewar, private val spacewarController: SpacewarController) : Scene(spacewar) {
    override val camera: Camera = Camera(this, 56f)

    private val guiMainMenu: GuiContainer = GuiMainMenu(this, spacewar.fontRenderer, spacewar.textureManager)

    private lateinit var spaceshipEntity: Entity

    override fun createScene() {
        this.spaceshipEntity = addEntity(EntitySpaceshipStatic(this), 0f, 0f, 0f)
        this.camera.apply {
            val bounds = this@SceneMainMenu.spaceshipEntity.getBounds()
            setRotation(0f, -30f)
            setPosition(
                spaceshipEntity.center.x,
                spaceshipEntity.center.y - (bounds.maxY - bounds.minY) * 0.15f,
                spaceshipEntity.center.y - (bounds.maxZ - bounds.minZ) * 1.5f
            )
        }

        super.setGui(this.guiMainMenu)
    }

    override fun update(time: Float) {
        super.update(time)
        this.spaceshipEntity.update(time)
    }

    fun onEnterBtnClicked() {
        spacewarController.loadInGameScene()
    }

    fun onLeaveBtnClicked() {
        this.spacewar.shutdown()
    }
}