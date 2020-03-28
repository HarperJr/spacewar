package com.harper.spacewar.main.entity.impl

import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.scene.Scene
import com.harper.spacewar.main.sprite.Sprite
import org.joml.AABBf

class EntitySpaceshipStatic(scene: Scene) : Entity(scene) {
    override val sprites: List<Sprite> = mutableListOf()

    init {
        super.axisAlignedBox = AABBf(0f, 0f, 0f, 80f, 25f, 100f)
    }
}