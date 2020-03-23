package com.harper.spacewar.main.scene

import com.conceptic.firefly.app.screen.Key
import com.harper.spacewar.main.Spacewar
import com.harper.spacewar.main.SpacewarController
import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.entity.SpaceshipEntity
import com.harper.spacewar.main.gui.impl.GuiInGame
import com.harper.spacewar.main.gui.impl.GuiInGameMenu

class SceneInGame(spacewar: Spacewar, spacewarController: SpacewarController) : Scene(spacewar) {
    private val guiInGame: GuiInGame = GuiInGame(spacewar.fontDrawer, spacewar.textureManager)
    private val guiInGameMenu: GuiInGameMenu = GuiInGameMenu(spacewar.fontDrawer, spacewar.textureManager)
    private lateinit var playerEntity: SpaceshipEntity

    private val enemies: List<Entity> = mutableListOf()

    override fun createScene() {
        this.playerEntity = addEntity(SpaceshipEntity(renderManager), 0f, 0f, 0f)
            .also { it.rotate(0f, 180f) } as SpaceshipEntity
        this.guiContainer = this.guiInGame
    }

    override fun update(time: Float) {
        val entityBounds = playerEntity.getBounds()
        this.spacewar.camera.apply {
            setRotation(25f, 0f)
            setPosition(
                (entityBounds.minX + entityBounds.maxX) / 2f,
                (entityBounds.minY + entityBounds.maxY) / 2f + (entityBounds.maxY - entityBounds.minY) * 2,
                (entityBounds.minZ + entityBounds.maxZ) / 2f - (entityBounds.maxZ - entityBounds.minZ) / 1.5f
            )
        }

        super.update(time)
    }

    override fun onKeyPressed(key: Key) {

    }

    override fun onKeyReleased(key: Key) {

    }

    companion object {
        private const val ENEMIES_MAX_COUNT = 10
        private const val TICKS_PER_SECOND = 1
        private const val ENEMY_SPAWN_THRESHOLD = 5000f
    }
}
