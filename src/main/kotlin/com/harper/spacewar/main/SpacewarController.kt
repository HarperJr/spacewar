package com.harper.spacewar.main

import com.harper.spacewar.logging.Logger
import com.harper.spacewar.main.gl.shader.definition.GuiShaderDefinition
import com.harper.spacewar.main.gl.shader.impl.GuiShader
import com.harper.spacewar.main.gl.shader.provider.ShaderProvider
import com.harper.spacewar.main.resolution.ScaledResolution
import com.harper.spacewar.main.resource.ResourceRegistry
import com.harper.spacewar.main.scene.Scene
import com.harper.spacewar.main.scene.SceneInGame
import com.harper.spacewar.main.scene.SceneMainMenu
import org.joml.Matrix4f

class SpacewarController(private val spacewar: Spacewar) {
    private val logger = Logger.getLogger<SpacewarController>()
    private val scaledResolution: ScaledResolution
        get() = spacewar.scaledResolution
    private lateinit var guiShader: GuiShader

    private var resourceRegistry: ResourceRegistry = spacewar.resourceRegistry
    private var currentScene: Scene? = null
    private var sceneIsDirty: Boolean = false

    fun initialize() {
        this.guiShader = ShaderProvider.provide(GuiShaderDefinition)
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
            currentScene?.update(time)
        } else renderStandBy()
    }

    fun destroy() {

    }

    fun loadInGameScene() {
        setScene(SceneInGame(spacewar, this))
    }

    fun loadMainMenuScene() {
        setScene(SceneMainMenu(spacewar, this))
    }

    fun restartScene() {
        this.currentScene?.destroy()
        this.sceneIsDirty = true
    }

    private fun setScene(newScene: Scene) {
        this.currentScene?.destroy()
        this.currentScene = newScene
        this.sceneIsDirty = true
    }

    private fun renderStandBy() {
        val projectionMatrix =
            Matrix4f().ortho(
                0f,
                scaledResolution.scaledWidth,
                scaledResolution.scaledHeight,
                0f,
                0.2f,
                1000f
            )
        val modelMatrix = Matrix4f()
            .apply { translate(0f, 0f, -100f) }
        val viewMatrix = Matrix4f()

        guiShader.use<GuiShader> {
            bindTexture(0)
            bindMatrices(modelMatrix, viewMatrix, projectionMatrix)
            spacewar.fontRenderer.drawCenteredText(
                "Please stand by...",
                scaledResolution.scaledWidth / 2f,
                scaledResolution.scaledHeight / 2f,
                0xffffffff,
                3f
            )
        }
    }
}