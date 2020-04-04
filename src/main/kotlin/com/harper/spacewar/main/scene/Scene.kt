package com.harper.spacewar.main.scene

import com.harper.spacewar.controls.Keyboard
import com.harper.spacewar.controls.Mouse
import com.harper.spacewar.display.Key
import com.harper.spacewar.logging.Logger
import com.harper.spacewar.main.Spacewar
import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.entity.EntityLiving
import com.harper.spacewar.main.entity.particle.EntityParticle
import com.harper.spacewar.main.gui.GuiContainer
import com.harper.spacewar.main.resolution.ScaledResolution
import com.harper.spacewar.main.scene.renderer.RenderManager

abstract class Scene(protected val spacewar: Spacewar) {
    var scaledResolution: ScaledResolution = spacewar.scaledResolution
        private set

    private val renderManager: RenderManager
        get() = spacewar.renderManager

    protected val logger = Logger.getLogger<SceneMainMenu>()

    protected var needsUpdates: Boolean = true

    private val entities: MutableMap<Int, Entity> = mutableMapOf()
    private val entitiesToBeAdded: MutableMap<Int, Entity> = mutableMapOf()
    private val entitiesToBeRemoved: MutableMap<Int, Entity> = mutableMapOf()
    private var guiContainer: GuiContainer? = null
    private var needsToUpdateGui: Boolean = true
    private var nextEntityId: Int = 0

    abstract val camera: Camera

    abstract fun createScene()

    open fun onEntityWasKilled(entity: EntityLiving) {
        /** No implementation **/
    }

    open fun onKeyPressed(key: Key) {
        /** No implementation **/
    }

    open fun onKeyReleased(key: Key) {
        /** No implementation **/
    }

    open fun onMouseClicked(x: Float, y: Float) {
        /** No implementation **/
    }

    open fun onMouseMoved(x: Float, y: Float) {
        /** No implementation **/
    }

    open fun onMousePressed(x: Float, y: Float) {
        /** No implementation **/
    }

    open fun onMouseScrolled(x: Float, y: Float) {
        /** No implementation **/
    }

    open fun update(time: Float) {
        if (this.scaledResolution != spacewar.scaledResolution || needsToUpdateGui) {
            this.scaledResolution = spacewar.scaledResolution
            guiContainer?.onResolutionChanged(this.scaledResolution)
            this.needsToUpdateGui = false
        }

        this.camera.update()

        for ((_, entity) in this.entities) {
            if (this.needsUpdates) {
                if (entity is EntityLiving && entity.isDead) {
                    this.removeEntity(entity)
                } else entity.update(time)

                if (entity is EntityParticle && entity.isEnded) {
                    this.entitiesToBeRemoved[entity.id] = entity
                }
            }
            renderManager.renderEntity(entity, this.camera)
        }

        if (this.guiContainer != null)
            renderGui(time)

        if (this.needsUpdates) {
            for ((key, entity) in this.entitiesToBeAdded) {
                this.entities[key] = entity
            }
            this.entitiesToBeAdded.clear()

            for ((key, entity) in this.entitiesToBeRemoved) {
                if (entity is EntityLiving)
                    onEntityWasKilled(entity)
                this.entities.remove(key)
            }
            this.entitiesToBeRemoved.clear()
        }

        while (Keyboard.next()) {
            val event = Keyboard.event()
            val key = Keyboard.key
            when (event) {
                Keyboard.Event.PRESS -> onKeyPressed(key)
                Keyboard.Event.RELEASE -> onKeyReleased(key)
                else -> {
                    /** No implementation **/
                }
            }
        }
    }

    open fun destroy() {
        this.entities.clear()
        this.nextEntityId = 0
        this.guiContainer = null
    }

    fun addEntity(entity: Entity, x: Float, y: Float, z: Float): Entity {
        this.entitiesToBeAdded[this.nextEntityId] = entity
            .also {
                it.create(this.nextEntityId, x, y, z)
            }
        this.nextEntityId++
        return entity
    }

    fun removeEntity(entity: Entity) {
        this.entitiesToBeRemoved[entity.id] = entity
    }

    fun getEntities(): Collection<Entity> = this.entities.values

    fun getEntitiesCollidedExcept(entity: Entity, exceptEntities: List<Entity>): List<Entity> {
        val collidedEntities = mutableListOf<Entity>()
        for ((_, e) in this.entities) {
            if (exceptEntities.contains(e) ||
                this.entitiesToBeRemoved.containsKey(e.id)
            ) continue

            val axisAlignedBB = e.getBounds()
            val collided = when (entity) {
                is EntityLiving ->
                    axisAlignedBB.testPoint(
                        entity.center.x + entity.lookAt.x,
                        entity.center.y + entity.lookAt.y,
                        entity.center.z + entity.lookAt.z
                    )
                else -> entity.getBounds().testAABB(e.getBounds())
            }

            if (collided)
                collidedEntities.add(e)
        }
        return collidedEntities
    }

    protected fun setGui(gui: GuiContainer) {
        if (this.guiContainer != gui) {
            this.guiContainer = gui
            this.requestGuiUpdate()
        }
    }

    protected fun requestGuiUpdate() {
        this.needsToUpdateGui = true
    }

    private fun renderGui(time: Float) {
        val (scaledWidth, scaledHeight) = scaledResolution
        val mouseX = Mouse.xPos * scaledWidth / spacewar.displayWidth
        val mouseY = scaledHeight - Mouse.yPos * scaledHeight / spacewar.displayHeight

        while (Mouse.next()) {
            val mouseEvent = Mouse.event()
            when (mouseEvent) {
                Mouse.Event.MOVE -> {
                    guiContainer?.onMoved(mouseX, mouseY)
                    onMouseMoved(mouseX, mouseY)
                }
                Mouse.Event.CLICK -> {
                    guiContainer?.onClicked(mouseX, mouseY)
                    onMouseClicked(mouseX, mouseY)
                }
                Mouse.Event.SCROLL -> {
                    onMouseScrolled(Mouse.scrollX, Mouse.scrollY)
                }
                Mouse.Event.PRESS -> {
                    onMousePressed(mouseX, mouseY)
                }
                else -> {
                    /** Not implemented, just skip **/
                }
            }
        }

        renderManager.renderGui(this.scaledResolution, guiContainer)
    }
}