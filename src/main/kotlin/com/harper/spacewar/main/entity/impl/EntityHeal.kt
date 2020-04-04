package com.harper.spacewar.main.entity.impl

import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.scene.SceneInGame
import org.joml.AABBf

class EntityHeal(sceneInGame: SceneInGame, val healPoints: Float) : Entity(sceneInGame) {
    private var rotYaw = 0f
    private var prevYaw = 0f

    init {
        super.axisAlignedBox = AABBf(0f, 0f, 0f, 2f, 2f, 2f)
    }

    override fun update(time: Float) {
        this.rotYaw = (this.prevYaw + (this.rotYaw - this.prevYaw) * time) % 360f
        super.rotate(this.rotYaw, 0f)

        this.prevYaw = this.rotYaw
        this.rotYaw += 0.4f
    }
}