package com.harper.spacewar.main.entity.impl

import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.scene.Scene
import com.harper.spacewar.main.entity.sprite.Sprite
import com.harper.spacewar.main.scene.SceneMainMenu
import org.joml.AABBf

class EntitySpaceshipStatic(scene: Scene) : Entity(scene) {
    private var rotYaw: Float = 0f
    private var prevYaw: Float = 0f

    init {
        super.axisAlignedBox = AABBf(0f, 0f, 0f, 10f, 2f, 10f)
    }

    override fun update(time: Float) {
        this.rotYaw = (this.prevYaw + (this.rotYaw - this.prevYaw) * time) % 360f
        super.rotate(this.rotYaw, 0f)

        this.prevYaw = this.rotYaw
        this.rotYaw += 0.1f
    }
}