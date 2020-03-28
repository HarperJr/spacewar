package com.harper.spacewar.main.entity

import com.harper.spacewar.main.scene.Camera
import com.harper.spacewar.main.scene.Scene
import com.harper.spacewar.main.sprite.Sprite
import com.harper.spacewar.utils.quatFromEuler
import org.joml.AABBf
import org.joml.Quaternionf
import org.joml.Vector3f

abstract class Entity(private val scene: Scene) {
    abstract val sprites: List<Sprite>

    var id: Int = 0

    val position: Vector3f = Vector3f(0f)
    val center: Vector3f
        get() {
            return Vector3f(
                (axisAlignedBox.minX + axisAlignedBox.maxX) / 2f,
                (axisAlignedBox.minY + axisAlignedBox.maxY) / 2f,
                (axisAlignedBox.minZ + axisAlignedBox.maxZ) / 2f
            )
        }

    protected val camera: Camera
        get() = scene.camera

    val rotation: Quaternionf = Quaternionf()

    protected var axisAlignedBox: AABBf = AABBf(0f, 0f, 0f, 1f, 1f, 1f)

    open fun create(x: Float, y: Float, z: Float) {
        setPosition(x, y, z)
    }

    open fun update(time: Float) {
        for (sprite in sprites)
            sprite.update()
    }

    fun setPosition(x: Float, y: Float, z: Float) {
        this.position.set(x, y, z)
        this.updateAxisAlignedBoxPosition()
    }

    open fun move(x: Float, y: Float, z: Float) {
        this.setPosition(this.position.x + x, this.position.y + y, this.position.z + z)
    }

    open fun rotate(yaw: Float, pitch: Float) {
        this.rotation.set(quatFromEuler(yaw, pitch))
    }

    fun getBounds(): AABBf {
        return this.axisAlignedBox
    }

    private fun updateAxisAlignedBoxPosition() {
        this.axisAlignedBox.translate(
            this.position.x - axisAlignedBox.minX - (axisAlignedBox.maxX - axisAlignedBox.minX) / 2f,
            this.position.y - axisAlignedBox.minY - (axisAlignedBox.maxY - axisAlignedBox.minY) / 2f,
            this.position.z - axisAlignedBox.minZ - (axisAlignedBox.maxZ - axisAlignedBox.minZ) / 2f
        )
    }

    override fun equals(other: Any?): Boolean {
        return (other as? Entity)?.id == this.id
    }

    fun isCollidedWidth(entity: Entity): Boolean {
        return this.axisAlignedBox.testAABB(entity.axisAlignedBox)
    }
}