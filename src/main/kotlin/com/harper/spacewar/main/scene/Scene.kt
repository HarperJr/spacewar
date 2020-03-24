package com.harper.spacewar.main.scene

import com.harper.spacewar.controls.Keyboard
import com.harper.spacewar.controls.Mouse
import com.harper.spacewar.display.Key
import com.harper.spacewar.logging.Logger
import com.harper.spacewar.main.Camera
import com.harper.spacewar.main.Spacewar
import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gui.GuiContainer
import com.harper.spacewar.main.resolution.ScaledResolution
import com.harper.spacewar.main.scene.renderer.RenderManager

abstract class Scene(protected val spacewar: Spacewar) {
    val renderManager: RenderManager = RenderManager(spacewar)
    var scaledResolution: ScaledResolution = spacewar.scaledResolution
        private set

    protected val logger = Logger.getLogger<SceneMainMenu>()

    private val entities: MutableMap<Int, Entity> = mutableMapOf()
    private var guiContainer: GuiContainer? = null
    private var needsToUpdateGui: Boolean = true
    private var nextEntityId: Int = 0

    abstract val camera: Camera

    abstract fun createScene()

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

    open fun onMouseScrolled(x: Float, y: Float) {
        /** No implementation **/
    }

    open fun update(time: Float) {
        if (this.scaledResolution != spacewar.scaledResolution || needsToUpdateGui) {
            this.scaledResolution = spacewar.scaledResolution
            guiContainer?.onResolutionChanged(this.scaledResolution)
            this.needsToUpdateGui = false
        }

        this.camera.update(time)

        for ((_, entity) in this.entities)
            entity.update(time)

        applyGuiProjection()
        if (this.guiContainer != null)
            renderGui(time)

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

    fun destroy() {
        this.nextEntityId = 0
        this.entities.clear()
        this.guiContainer = null
    }

    protected fun addEntity(entity: Entity, x: Float, y: Float, z: Float): Entity {
        this.entities[this.nextEntityId++] = entity
            .also { it.create(x, y, z) }
        return entity
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
                else -> {
                    /** Not implemented, just skip **/
                }
            }
        }

        guiContainer?.render(time)
    }

    private fun applyGuiProjection() {
        GlUtils.glMatrixMode(GlUtils.PROJECTION)
        GlUtils.glLoadIdentity()
        GlUtils.glOrtho(
            0.0,
            this.scaledResolution.scaledWidth.toDouble(),
            this.scaledResolution.scaledHeight.toDouble(),
            0.0,
            0.2,
            1000.0
        )
        GlUtils.glMatrixMode(GlUtils.MODELVIEW)
        GlUtils.glLoadIdentity()
        GlUtils.glTranslatef(0f, 0f, -100f)
    }
}