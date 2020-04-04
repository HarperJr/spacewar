package com.harper.spacewar.main.entity

import com.harper.spacewar.main.scene.Camera
import com.harper.spacewar.main.scene.Scene
import com.harper.spacewar.utils.quatFromEuler
import org.joml.AABBf
import org.joml.Quaternionf
import org.joml.Vector3f

abstract class Entity(private val scene: Scene) {
    var id: Int = 0

    val position: Vector3f = Vector3f(0f)
    val rotation: Quaternionf = Quaternionf()

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

    protected var axisAlignedBox: AABBf = AABBf(0f, 0f, 0f, 1f, 1f, 1f)

    abstract fun update(time: Float)

    open fun create(id: Int, x: Float, y: Float, z: Float) {
        setPosition(x, y, z)
        this.id = id
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

    private fun setPosition(x: Float, y: Float, z: Float) {
        this.position.set(x, y, z)
        this.updateAxisAlignedBoxPosition()
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

    override fun hashCode(): Int {
        var result = scene.hashCode()
        result = 31 * result + id
        result = 31 * result + position.hashCode()
        result = 31 * result + rotation.hashCode()
        result = 31 * result + axisAlignedBox.hashCode()
        return result
    }
}