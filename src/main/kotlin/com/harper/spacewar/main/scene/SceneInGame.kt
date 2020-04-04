package com.harper.spacewar.main.scene

import com.harper.spacewar.controls.Mouse
import com.harper.spacewar.display.Key
import com.harper.spacewar.main.Spacewar
import com.harper.spacewar.main.SpacewarController
import com.harper.spacewar.main.entity.EntityLiving
import com.harper.spacewar.main.entity.impl.EntityEnemy
import com.harper.spacewar.main.entity.impl.EntityHeal
import com.harper.spacewar.main.entity.impl.EntityMissile
import com.harper.spacewar.main.entity.impl.EntityPlayer
import com.harper.spacewar.main.gui.impl.GuiInGame
import com.harper.spacewar.main.gui.impl.GuiInGameMenu
import com.harper.spacewar.main.gui.impl.GuiPlayerKilled
import org.joml.Random
import kotlin.math.max
import kotlin.math.min

class SceneInGame(spacewar: Spacewar, private val spacewarController: SpacewarController) : Scene(spacewar) {
    private val guiInGame: GuiInGame = GuiInGame(this, spacewar.fontRenderer, spacewar.textureManager)
    private val guiInGameMenu: GuiInGameMenu = GuiInGameMenu(this, spacewar.fontRenderer, spacewar.textureManager)
    private val guiPlayerKilled: GuiPlayerKilled = GuiPlayerKilled(this, spacewar.fontRenderer, spacewar.textureManager)

    override val camera: Camera = Camera(this, 50f)

    val fps: Int
        get() = spacewar.fps

    private var isPaused = false
        set(value) {
            field = value
            super.needsUpdates = !field
        }

    var cameraRotYaw: Float = 0f
        private set

    var cameraRotPitch: Float = 0f
        private set

    var entityPlayerPoints: Int = 0
        private set

    private val enemies: MutableList<EntityEnemy> = mutableListOf()
    private val healEntities: MutableList<EntityHeal> = mutableListOf()
    lateinit var entityPlayer: EntityPlayer
        private set

    private var scrollFactor: Float = MIN_SCROLL_FACTOR
    private var prevMousePosX: Float = 0f
    private var prevMousePosY: Float = 0f

    private val currentTimeMillis: Long
        get() = System.currentTimeMillis()
    private var enemySpawnedPrevTimeMillis: Long = currentTimeMillis
    private var healSpawnedPrevTimeMillis: Long = currentTimeMillis

    private val random: Random = Random(1000)


    override fun createScene() {
        this.spacewar.setCursorVisible(false)
        this.entityPlayer = addEntity(EntityPlayer(this), 0f, 0f, 0f) as EntityPlayer
        this.entityPlayerPoints = 0
        super.setGui(this.guiInGame)
    }

    override fun update(time: Float) {
        if (!this.isPaused) {
            if (this.entityPlayer.isDead)
                onPlayerWasKilled()

            updateCameraControls(time)

            if (this.enemySpawnedPrevTimeMillis + ENEMY_SPAWN_THRESHOLD_MILLIS <= currentTimeMillis) {
                if (this.enemies.size < MAX_ENEMIES_COUNT)
                    spawnEnemy()
                this.enemySpawnedPrevTimeMillis = currentTimeMillis
            }

            if (this.healSpawnedPrevTimeMillis + HEAL_SPAWN_THRESHOLD_MILLIS <= currentTimeMillis) {
                if (this.healEntities.size < MAX_HEAL_ENTITY_COUNT)
                    spawnHeal()
                this.healSpawnedPrevTimeMillis = currentTimeMillis
            }

            val collidedEntities = this.getEntitiesCollidedExcept(entityPlayer, emptyList())
            for (e in collidedEntities)
                if (e is EntityHeal) {
                    if (this.entityPlayer.addHealth(e.healPoints))
                        super.removeEntity(e)
                }
        }

        updateCameraTransform()
        super.update(time)
    }

    private fun onPlayerWasKilled() {
        this.isPaused = true
        super.setGui(this.guiPlayerKilled)
        this.spacewar.setCursorVisible(this.isPaused)
    }

    override fun onMouseScrolled(x: Float, y: Float) {
        if (!this.isPaused)
            this.scrollFactor = min(MAX_SCROLL_FACTOR, max(MIN_SCROLL_FACTOR, this.scrollFactor + Mouse.scrollY / -10f))
    }

    override fun onEntityWasKilled(entity: EntityLiving) {
        if (entity is EntityEnemy) {
            if (entity.killedBy is EntityMissile) {
                val missileProducer = (entity.killedBy as EntityMissile).producerEntity
                if (missileProducer is EntityPlayer)
                    this.entityPlayerPoints++
            }

            this.enemies.remove(entity)
        }
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

    override fun destroy() {
        super.destroy()
        this.isPaused = false
        this.enemies.clear()
        this.healEntities.clear()
        this.cameraRotYaw = 0f
        this.cameraRotPitch = 0f
    }

    fun onContinueBtnClicked() {
        switchPausedState()
    }

    fun onLeaveBtnClicked() {
        this.spacewarController.loadMainMenuScene()
    }

    fun onRestartBtnClicked() {
        this.spacewarController.restartScene()
    }

    private fun spawnEnemy() {
        val playerPos = this.entityPlayer.position
        val entityEnemy = addEntity(
            EntityEnemy(this, 10f),
            playerPos.x + SPAWN_RADIUS / 2f - random.nextInt(SPAWN_RADIUS),
            playerPos.y + SPAWN_RADIUS / 2f - random.nextInt(SPAWN_RADIUS),
            playerPos.z + SPAWN_RADIUS / 2f - random.nextInt(SPAWN_RADIUS)
        ) as EntityEnemy

        this.enemies.add(entityEnemy)
    }

    private fun spawnHeal() {
        val playerPos = this.entityPlayer.position
        val randVal = if (this.entityPlayer.healthPercent >= 0.4f) {
            random.nextInt(400)
        } else random.nextInt(200)
        val entityHeal = addEntity(
            EntityHeal(this, 0.5f),
            playerPos.x + randVal / 2f - randVal,
            playerPos.y + randVal / 2f - randVal,
            playerPos.z + randVal / 2f - randVal
        ) as EntityHeal

        this.healEntities.add(entityHeal)
    }

    private fun updateCameraControls(time: Float) {
        val mouseXPos = (Mouse.xPos - this.spacewar.displayWidth / 2f) / this.spacewar.displayWidth / 2f
        val mouseYPos = (Mouse.yPos - this.spacewar.displayHeight / 2f) / this.spacewar.displayHeight / 2f
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

    private fun updateCameraTransform() {
        val playerCenterPos = entityPlayer.center
        val bounds = entityPlayer.getBounds()
        this.camera.apply {
            setRotation(this@SceneInGame.cameraRotYaw, this@SceneInGame.cameraRotPitch)
            setLookAt(playerCenterPos.x, playerCenterPos.y, playerCenterPos.z)
            setPosition(
                playerCenterPos.x,
                playerCenterPos.y - (bounds.maxY - bounds.minY) * 2.55f * scrollFactor,
                playerCenterPos.z - (bounds.maxZ - bounds.minZ) * 1.55f * scrollFactor
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
        private const val MIN_SCROLL_FACTOR = 3f
        private const val MAX_SCROLL_FACTOR = 6f
        private const val MOUSE_SENSITIVITY = 0.15f
        private const val MAX_ENEMIES_COUNT = 100
        private const val MAX_HEAL_ENTITY_COUNT = 20
        private const val ENEMY_SPAWN_THRESHOLD_MILLIS = 2000L
        private const val HEAL_SPAWN_THRESHOLD_MILLIS = 10000L
        private const val SPAWN_RADIUS = 2000
    }
}