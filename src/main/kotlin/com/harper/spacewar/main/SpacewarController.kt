package com.harper.spacewar.main

import com.harper.spacewar.logging.Logger
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.resolution.ScaledResolution
import com.harper.spacewar.main.resource.ResourceRegistry
import com.harper.spacewar.main.scene.Scene
import com.harper.spacewar.main.scene.SceneInGame
import com.harper.spacewar.main.scene.SceneMainMenu

class SpacewarController(private val spacewar: Spacewar) {
    private val logger = Logger.getLogger<SpacewarController>()
    private val scaledResolution: ScaledResolution
        get() = spacewar.scaledResolution
    private var resourceRegistry: ResourceRegistry = spacewar.resourceRegistry
    private var currentScene: Scene? = null

    private var sceneIsDirty: Boolean = false

    fun initialize() {
        setScene(SceneMainMenu(spacewar, this))
    }

    fun update(time: Float) {
        if (this.sceneIsDirty) {
            if (resourceRegistry.areResourcesLoaded()) {
                logger.info("Scene is updated")
                this.sceneIsDirty = false
                currentScene?.createScene()
            } else {
                resourceRegistry.loadResources()
            }
        }

        if (!this.sceneIsDirty) {
            spacewar.camera.update(time)
            currentScene?.update(time)
        } else {
            renderStandBy()
        }
    }

    fun destroy() {

    }

    fun loadInGameScene() {
        setScene(SceneInGame(spacewar, this))
    }

    fun loadMainMenuScene() {
        setScene(SceneMainMenu(spacewar, this))
    }

    private fun setScene(newScene: Scene) {
        this.currentScene?.destroy()
        this.currentScene = newScene
        this.sceneIsDirty = true
    }

    private fun renderStandBy() {
        GlUtils.glMatrixMode(GlUtils.PROJECTION)
        GlUtils.glLoadIdentity()
        GlUtils.glOrtho(
            0.0,
            scaledResolution.scaledWidth.toDouble(),
            scaledResolution.scaledHeight.toDouble(),
            0.0,
            0.2,
            1000.0
        )
        GlUtils.glMatrixMode(GlUtils.MODELVIEW)
        GlUtils.glLoadIdentity()
        GlUtils.glTranslatef(0f, 0f, -100f)

        spacewar.fontRenderer.drawCenteredText(
            "Please stand by...",
            scaledResolution.scaledWidth / 2f,
            scaledResolution.scaledHeight / 2f,
            0xffffffff,
            3f
        )
    }
}