package com.harper.spacewar.main.scene

import com.harper.spacewar.controls.Mouse
import com.harper.spacewar.logging.Logger
import com.harper.spacewar.main.Spacewar
import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gui.GuiContainer
import com.harper.spacewar.main.resolution.ScaledResolution
import com.harper.spacewar.main.scene.renderer.RenderManager
import kotlin.reflect.KClass

abstract class Scene(protected val spacewar: Spacewar) {
    protected val logger = Logger.getLogger<SceneMainMenu>()
    protected val renderManager: RenderManager = RenderManager(spacewar)
    protected var guiContainer: GuiContainer? = null

    private val entities: MutableMap<KClass<*>, Entity> = mutableMapOf()
    private var scaledResolution: ScaledResolution = spacewar.scaledResolution

    abstract fun createScene()

    open fun update(time: Float) {
        GlUtils.glMatrixMode(GlUtils.PROJECTION)
        GlUtils.glLoadIdentity()
        GlUtils.glPerspective(45f, scaledResolution.scaledWidth / scaledResolution.scaledHeight, 0.1f, 1000f)

        GlUtils.glMatrixMode(GlUtils.MODELVIEW)
        GlUtils.glLoadIdentity()
        GlUtils.glTranslatef(0f, 0f, -1f)

        for ((_, entity) in this.entities)
            entity.update(time)

        if (this.guiContainer != null)
            renderGui(time)
    }

    fun addEntity(entity: Entity, x: Float, y: Float, z: Float): Entity {
        val savedEntity = this.entities[entity::class]
        if (savedEntity != null) {
            savedEntity.setPosition(x, y, z)
        } else {
            this.entities[entity::class] = entity.also {
                it.create(x, y, z)
            }
        }

        return entity
    }

    fun destroy() {
        this.entities.clear()
        this.guiContainer = null
    }

    private fun renderGui(time: Float) {
        if (scaledResolution != spacewar.scaledResolution) {
            scaledResolution = spacewar.scaledResolution
            guiContainer?.onResolutionChanged(scaledResolution)
        }

        val (scaledWidth, scaledHeight) = scaledResolution
        val mouseX = Mouse.xPos * scaledWidth / spacewar.displayWidth
        val mouseY = scaledHeight - Mouse.yPos * scaledHeight / spacewar.displayHeight

        while (Mouse.next()) {
            val mouseEvent = Mouse.event()
            if (mouseEvent == Mouse.Event.MOVE) {
                guiContainer?.onMoved(mouseX, mouseY)
            } else if (mouseEvent == Mouse.Event.CLICK) {
                guiContainer?.onClicked(mouseX, mouseY)
            }
        }

        GlUtils.glMatrixMode(GlUtils.PROJECTION)
        GlUtils.glLoadIdentity()
        GlUtils.glOrtho(0.0, scaledWidth.toDouble(), scaledHeight.toDouble(), 0.0, 0.2, 1000.0)
        GlUtils.glMatrixMode(GlUtils.MODELVIEW)
        GlUtils.glLoadIdentity()
        GlUtils.glTranslatef(0f, 0f, -100f)

        guiContainer?.render(time)
    }
}