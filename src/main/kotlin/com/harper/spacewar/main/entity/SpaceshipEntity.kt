package com.harper.spacewar.main.entity

import com.harper.spacewar.main.scene.renderer.RenderManager
import org.joml.AABBf

open class SpaceshipEntity(renderManager: RenderManager) : Entity(renderManager) {
    init {
        super.axisAlignedBox = AABBf(0f, 0f, 0f, 80f, 25f, 100f)
    }
}