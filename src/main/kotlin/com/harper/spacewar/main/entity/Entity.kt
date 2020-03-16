package com.harper.spacewar.main.entity

import com.harper.spacewar.main.scene.Scene
import org.joml.AABBf
import org.joml.Vector3f

abstract class Entity(private val scene: Scene) {
    private val axisAlignedBox: AABBf = AABBf()
    private val position: Vector3f = Vector3f(0f)

    private var rotYaw: Float = 0f
    private var rotRoll: Float = 0f

    fun move(x: Float, y: Float, z: Float) {

    }

    fun rotate(yaw: Float, roll: Float) {
        this.rotYaw = (this.rotYaw + yaw * Math.PI.toFloat() / 180f) % 360f
        this.rotRoll = (this.rotRoll + roll * Math.PI.toFloat() / 180f) % 360f
    }
}