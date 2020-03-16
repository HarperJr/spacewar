package com.harper.spacewar.main.scene

class SceneDrawer(private val scene: Scene) {
    private var isDirty = true

    fun draw() {
       if (isDirty)
           scene.prepareScene()
    }
}