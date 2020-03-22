package com.harper.spacewar.main

import com.harper.spacewar.CameraSource
import com.harper.spacewar.main.resolution.ScaledResolution
import org.joml.Matrix4f
import org.joml.Vector3f
import kotlin.math.cos
import kotlin.math.sin

class Camera(private val spacewar: Spacewar, private val fov: Float) : CameraSource {
    override val projection: Matrix4f
        get() = projectionMatrix

    override val view: Matrix4f
        get() = viewMatrix

    private val scaledResolution: ScaledResolution
        get() = spacewar.scaledResolution

    private val upVector = Vector3f(0f, 1f, 0f)

    private val lookAt: Vector3f
        get() = getLookAtVector()

    var position: Vector3f = Vector3f(0f)
        private set

    private var projectionMatrix = Matrix4f()
    private var viewMatrix = Matrix4f()

    private var pitch: Float = 0f
    private var yaw: Float = 0f

    override fun update(time: Float) {
        this.projectionMatrix.identity()
        this.projectionMatrix.setPerspective(
            fov,
            scaledResolution.scaledWidth / scaledResolution.scaledHeight,
            0.1f,
            1000f
        )

        this.viewMatrix.identity()
        val center = Vector3f(
            this.position.x + this.lookAt.x,
            this.position.y + this.lookAt.y,
            this.position.z + this.lookAt.z
        )
        this.viewMatrix.lookAt(this.position, center, this.upVector)
    }

    override fun setPosition(x: Float, y: Float, z: Float) {
        this.position.set(x, y, z)
    }

    override fun setRotation(pitch: Float, yaw: Float) {
        this.pitch = pitch
        this.yaw = yaw
    }

    private fun getLookAtVector(): Vector3f {
        val f = cos(-yaw / 180f * Math.PI - Math.PI)
        val f1 = sin(-yaw / 180f * Math.PI - Math.PI)
        val f2 = -cos(-pitch / 180f * Math.PI)
        val f3 = sin(-pitch / 180f * Math.PI)
        return Vector3f((f1 * f2).toFloat(), f3.toFloat(), (f * f2).toFloat())
    }
}