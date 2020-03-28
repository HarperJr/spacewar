package com.harper.spacewar.main.scene

import com.harper.spacewar.controls.Mouse
import com.harper.spacewar.display.Key
import com.harper.spacewar.main.Spacewar
import com.harper.spacewar.main.SpacewarController
import com.harper.spacewar.main.entity.impl.EntityEnemy
import com.harper.spacewar.main.entity.impl.EntityPlayer
import com.harper.spacewar.main.gui.impl.GuiInGame
import com.harper.spacewar.main.gui.impl.GuiInGameMenu
import org.joml.Random
import kotlin.math.max
import kotlin.math.min

class SceneInGame(spacewar: Spacewar, private val spacewarController: SpacewarController) : Scene(spacewar) {
    private val guiInGame: GuiInGame = GuiInGame(this, spacewar.fontRenderer, spacewar.textureManager)
    private val guiInGameMenu: GuiInGameMenu = GuiInGameMenu(this, spacewar.fontRenderer, spacewar.textureManager)

    override val camera: Camera = Camera(this, 50f)

    val fps: Int
        get() = spacewar.fps

    var isPaused = false
        private set

    var cameraRotYaw: Float = 0f
        private set

    var cameraRotPitch: Float = 0f
        private set

    private val enemies: MutableList<EntityEnemy> = mutableListOf()
    lateinit var entityPlayer: EntityPlayer
        private set

    private var scrollFactor: Float = MIN_SCROLL_FACTOR
    private var prevMousePosX: Float = 0f
    private var prevMousePosY: Float = 0f

    private val currentTimeMillis: Long
        get() = System.currentTimeMillis()
    private var prevTimeMillis: Long = currentTimeMillis

    private val random: Random = Random(1000)


    override fun createScene() {
        this.spacewar.setCursorVisible(false)
        this.entityPlayer = addEntity(EntityPlayer(this), 0f, 0f, 0f) as EntityPlayer
        super.setGui(this.guiInGame)
    }

    override fun update(time: Float) {
        if (!this.isPaused) {
            updateCameraControls(time)

            if (this.prevTimeMillis + ENEMY_SPAWN_THRESHOLD_MILLIS <= currentTimeMillis) {
                if (this.enemies.size < MAX_ENEMIES_COUNT)
                    spawnEnemy()
                this.prevTimeMillis = currentTimeMillis
            }
        }

        updateCamera()
        super.update(time)
    }

    override fun onMouseScrolled(x: Float, y: Float) {
        if (!this.isPaused)
            this.scrollFactor = min(MAX_SCROLL_FACTOR, max(MIN_SCROLL_FACTOR, this.scrollFactor + Mouse.scrollY / -10f))
    }

    override fun onKeyPressed(key: Key) {
        when (key) {
            Key.ESC -> switchPausedState()
            Key.W -> this.entityPlayer.accelerate = true
            Key.F3 -> {
                if (!isPaused) {
                    this.guiInGame.switchDebuggingState()
                    super.requestGuiUpdate()
                }
            }
            else -> {
                /** Not implemented, just skip **/
            }
        }
    }

    override fun onKeyReleased(key: Key) {
        if (key == Key.W)
            this.entityPlayer.accelerate = false
    }

    override fun onMousePressed(x: Float, y: Float) {
        this.entityPlayer.onMousePressed()
    }

    fun onContinueBtnClicked() {
        switchPausedState()
    }

    fun onSaveBtnClicked() {

    }

    fun onLeaveBtnClicked() {
        this.spacewarController.loadMainMenuScene()
    }

    private fun spawnEnemy() {
        val playerPos = this.entityPlayer.position
        val entityEnemy = addEntity(
            EntityEnemy(this, 2f, EntityEnemy.Type.COMMON),
            playerPos.x + 1000 - random.nextInt(500),
            playerPos.y + 1000 - random.nextInt(500),
            playerPos.z + 1000 - random.nextInt(500)
        ) as EntityEnemy

        this.enemies.add(entityEnemy)
    }

    private fun updateCameraControls(time: Float) {
        val mouseXPos = (Mouse.xPos - this.spacewar.displayWidth / 2f) / this.spacewar.displayWidth
        val mouseYPos = (Mouse.yPos - this.spacewar.displayHeight / 2f) / this.spacewar.displayHeight
        val deltaYaw = ((mouseXPos - prevMousePosX) * time) * MOUSE_SENSITIVITY * 1000f
        val deltaPitch = ((mouseYPos - prevMousePosY) * time) * MOUSE_SENSITIVITY * 1000f
        this.cameraRotYaw = (this.cameraRotYaw - deltaYaw) % 360f
        this.cameraRotPitch = (this.cameraRotPitch + deltaPitch) % 360f

        if (this.cameraRotPitch > 90f)
            this.cameraRotPitch = 90f

        if (this.cameraRotPitch < -90f)
            this.cameraRotPitch = -90f

        this.prevMousePosX = mouseXPos
        this.prevMousePosY = mouseYPos
    }

    private fun updateCamera() {
        val entityCenterPos = entityPlayer.center
        val entityBounds = entityPlayer.getBounds()
        this.camera.apply {
            setRotation(this@SceneInGame.cameraRotYaw, this@SceneInGame.cameraRotPitch)
            setLookAt(entityCenterPos.x, entityCenterPos.y, entityCenterPos.z)
            setPosition(
                entityCenterPos.x,
                entityCenterPos.y + (entityBounds.maxY - entityBounds.minY) * 1.5f * scrollFactor,
                entityCenterPos.z + (entityBounds.maxZ - entityBounds.minZ) * 1.5f * scrollFactor
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
        private const val MIN_SCROLL_FACTOR = 2f
        private const val MAX_SCROLL_FACTOR = 5f
        private const val MOUSE_SENSITIVITY = 0.1f
        private const val MAX_ENEMIES_COUNT = 10
        private const val ENEMY_SPAWN_THRESHOLD_MILLIS = 3000
    }
}