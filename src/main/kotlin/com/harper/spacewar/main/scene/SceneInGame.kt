package com.harper.spacewar.main.scene

import com.harper.spacewar.controls.Mouse
import com.harper.spacewar.display.Key
import com.harper.spacewar.main.Spacewar
import com.harper.spacewar.main.SpacewarController
import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.entity.PlayerEntity
import com.harper.spacewar.main.gui.impl.GuiInGame
import com.harper.spacewar.main.gui.impl.GuiInGameMenu
import org.joml.Vector3f
import kotlin.math.max
import kotlin.math.min

class SceneInGame(spacewar: Spacewar, private val spacewarController: SpacewarController) : Scene(spacewar) {
    private val guiInGame: GuiInGame = GuiInGame(this, spacewar.fontRenderer, spacewar.textureManager)
    private val guiInGameMenu: GuiInGameMenu = GuiInGameMenu(this, spacewar.fontRenderer, spacewar.textureManager)

    lateinit var playerEntity: PlayerEntity
        private set

    val fps: Int
        get() = spacewar.fps

    var isPaused = false
        private set

    private val enemies: List<Entity> = mutableListOf()

    private var scrollFactor: Float = MIN_SCROLL_FACTOR
    private var cameraRotYaw: Float = 0f
    private var cameraRotPitch: Float = 0f
    private var prevMousePosX: Float = 0f
    private var prevMousePosY: Float = 0f


    override fun createScene() {
        this.spacewar.setCursorVisible(false)
        this.playerEntity = addEntity(PlayerEntity(this, renderManager), 0f, 0f, 0f)
            .also { it.rotate(0f, 180f) } as PlayerEntity
        super.setGui(this.guiInGame)
    }

    override fun update(time: Float) {
        if (!this.isPaused) {
            val adjustedXPos = (Mouse.xPos - this.spacewar.displayWidth / 2f) / this.spacewar.displayWidth * 1000f
            val adjustedYPos = (Mouse.yPos - this.spacewar.displayHeight / 2f) / this.spacewar.displayHeight * 1000f
            val deltaYaw = ((adjustedXPos - prevMousePosX) * time) * MOUSE_SENSITIVITY
            val deltaPitch = ((adjustedYPos - prevMousePosY) * time) * MOUSE_SENSITIVITY
            this.cameraRotYaw = (this.cameraRotYaw + deltaYaw) % 360f
            this.cameraRotPitch = (this.cameraRotPitch - deltaPitch) % 360f

            this.prevMousePosX = adjustedXPos
            this.prevMousePosY = adjustedYPos
        }

        updateCamera()
        super.update(time)
    }

    override fun onMouseScrolled(x: Float, y: Float) {
        if (!this.isPaused)
            this.scrollFactor = min(MAX_SCROLL_FACTOR, max(MIN_SCROLL_FACTOR, this.scrollFactor + Mouse.scrollY / -10f))
    }

    override fun onKeyPressed(key: Key) {
        if (key == Key.ESC)
            switchPausedState()
        if (key == Key.F3 && !isPaused) {
            this.guiInGame.switchDebuggingState()
            super.requestGuiUpdate()
        }
    }

    override fun onKeyReleased(key: Key) {

    }

    fun onContinueBtnClicked() {
        switchPausedState()
    }

    fun onSaveBtnClicked() {

    }

    fun onLeaveBtnClicked() {
        this.spacewarController.loadMainMenuScene()
    }

    private fun updateCamera() {
        val entityBounds = playerEntity.getBounds()
        val entityCenter = Vector3f(
            (entityBounds.minX + entityBounds.maxX) / 2f,
            (entityBounds.minY + entityBounds.maxY) / 2f,
            (entityBounds.minZ + entityBounds.maxZ) / 2f
        )

        this.spacewar.camera.apply {
            setRotation(this@SceneInGame.cameraRotYaw, this@SceneInGame.cameraRotPitch)
            setPosition(
                entityCenter.x,
                entityCenter.y + (entityBounds.maxY - entityBounds.minY) * 2f * scrollFactor,
                entityCenter.z - (entityBounds.maxZ - entityBounds.minZ) * 1.5f * scrollFactor
            )
        }
    }

    private fun switchPausedState() {
        if (isPaused) {
            super.setGui(this.guiInGame)
        } else super.setGui(this.guiInGameMenu)
        this.isPaused = !this.isPaused
        this.spacewar.setCursorVisible(this.isPaused)
    }

    companion object {
        private const val MIN_SCROLL_FACTOR = 1f
        private const val MAX_SCROLL_FACTOR = 2f
        private const val MOUSE_SENSITIVITY = 0.1f
    }
}