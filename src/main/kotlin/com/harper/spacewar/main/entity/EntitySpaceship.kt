package com.harper.spacewar.main.entity

import com.harper.spacewar.main.scene.Scene
import org.joml.AABBf

open class EntitySpaceship(scene: Scene) : Entity(scene) {
    init {
        super.axisAlignedBox = AABBf(0f, 0f, 0f, 80f, 25f, 100f)
    }
}