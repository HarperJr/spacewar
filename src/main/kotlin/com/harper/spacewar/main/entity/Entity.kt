package com.harper.spacewar.main.entity

import com.harper.spacewar.main.scene.renderer.RenderManager
import org.joml.AABBf
import org.joml.Vector3f

abstract class Entity(private val renderManager: RenderManager) {
    private val axisAlignedBox: AABBf = AABBf()
    private val position: Vector3f = Vector3f(0f)

    var rotYaw: Float = 0f
        private set
    var rotRoll: Float = 0f
        private set

    fun create(x: Float, y: Float, z: Float) {
        this.axisAlignedBox.setMax(0.5f, 1f, 0.5f)
        setPosition(x, y, z)
    }

    fun update(time: Float) {
        // Do render
        renderManager.renderEntity(this, position.x, position.y, position.z)
    }

    fun setPosition(x: Float, y: Float, z: Float) {
        this.position.set(x, y, z)
        this.setAxisAlignedBoxPosition(x, y, z)
    }

    fun move(x: Float, y: Float, z: Float) {
        this.setPosition(this.position.x + x, this.position.y + y, this.position.z + z)
    }

    fun rotate(yaw: Float, roll: Float) {
        this.rotYaw = yaw * 180f / Math.PI.toFloat()
        this.rotRoll = roll * 180f / Math.PI.toFloat()
    }

    private fun setAxisAlignedBoxPosition(x: Float, y: Float, z: Float) {
        this.axisAlignedBox.translate(x - axisAlignedBox.minX, y - axisAlignedBox.minY, z - axisAlignedBox.minZ)
    }
}