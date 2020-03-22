package com.harper.spacewar.main

import com.harper.spacewar.logging.Logger
import com.harper.spacewar.main.resource.ResourceRegistry
import com.harper.spacewar.main.scene.Scene
import com.harper.spacewar.main.scene.SceneInGame
import com.harper.spacewar.main.scene.SceneMainMenu

class SpacewarController(private val spacewar: Spacewar) {
    private val logger = Logger.getLogger<SpacewarController>()
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

        if (!this.sceneIsDirty)
            currentScene?.update(time)
    }

    fun destroy() {

    }

    fun loadInGameScene() {
        setScene(SceneInGame(spacewar, this))
    }

    private fun setScene(newScene: Scene) {
        this.currentScene?.destroy()
        this.currentScene = newScene
        this.sceneIsDirty = true
    }
}