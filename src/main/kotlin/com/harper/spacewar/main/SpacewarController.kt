package com.harper.spacewar.main

import com.harper.spacewar.display.listener.DisplayListener
import com.harper.spacewar.main.scene.Scene
import com.harper.spacewar.main.scene.SceneMainMenu

class SpacewarController(spacewar: Spacewar) : DisplayListener {
    private var currentScene: Scene = SceneMainMenu(spacewar)

    override fun onInitialized() {

    }

    override fun onUpdated() {
        currentScene.onUpdated()
    }

    override fun onDestroyed() {

    }
}
