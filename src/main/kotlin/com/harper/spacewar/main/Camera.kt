package com.harper.spacewar.main

import com.harper.spacewar.CameraSource
import com.harper.spacewar.main.resolution.ScaledResolution
import org.joml.Matrix4f
import org.joml.Vector3f

class Camera(private val spacewar: Spacewar, private val fov: Float) : CameraSource {
    override val projection: Matrix4f
        get() = projectionMatrix

    override val view: Matrix4f
        get() = viewMatrix

    private val scaledResolution: ScaledResolution
        get() = spacewar.scaledResolution

    private val cameraCenter = Vector3f(0f)
    private val upVector = Vector3f(0f, 1f, 0f)

    var position: Vector3f = Vector3f(0f)
        private set
    private var projectionMatrix = Matrix4f()
    private var viewMatrix = Matrix4f()

    private var lookAt: Vector3f = Vector3f(0f, 0f, 1f)

    override fun update(time: Float) {
        this.projectionMatrix.identity()
        this.projectionMatrix.setPerspective(fov, scaledResolution.scaledWidth / scaledResolution.scaledHeight, 0.1f, 1000f)

        this.viewMatrix.identity()
        this.viewMatrix.lookAt(this.lookAt, this.cameraCenter, this.upVector)
    }

    override fun setPosition(x: Float, y: Float, z: Float) {
        position.set(x, y, z)
    }

    override fun setRotation(pitch: Float, yaw: Float, roll: Float) {
        this.lookAt.set(
            this.lookAt.x * pitch / 180f * Math.PI.toFloat(),
            this.lookAt.y * yaw / 180f * Math.PI.toFloat(),
            this.lookAt.z * roll / 180f * Math.PI.toFloat()
        )
    }
}