package com.harper.spacewar.main.scene

import com.harper.spacewar.logging.Logger
import com.harper.spacewar.utils.orthoNormalize
import com.harper.spacewar.utils.vecFromEuler
import org.joml.Matrix4f
import org.joml.Vector3f

class Camera(private val scene: Scene, private val fov: Float) {
    private val logger: Logger = Logger.getLogger<Camera>()

    val projection: Matrix4f
        get() = projectionMatrix

    val view: Matrix4f
        get() = viewMatrix

    val lookVec: Vector3f
        get() = vecFromEuler(this.yaw, this.pitch)

    val upVector: Vector3f = UP_VECTOR

    private val ratio: Float
        get() {
            return scene.scaledResolution.scaledWidth /
                    scene.scaledResolution.scaledHeight
        }

    private val lookAt: Vector3f = Vector3f()

    private var projectionMatrix = Matrix4f()
    private var viewMatrix = Matrix4f()

    private var yaw: Float = 0f
    private var pitch: Float = 0f

    private val position: Vector3f = Vector3f(0f)

    fun update() {
        this.projectionMatrix.identity()
        this.projectionMatrix.setPerspective(fov, ratio, 0.1f, 1000000f)

        val cameraDistance = this.position.distance(this.lookAt)
        val cameraPosition = Vector3f(
            -this.lookVec.x * cameraDistance + this.lookAt.x,
            -this.lookVec.y * cameraDistance + this.lookAt.y,
            -this.lookVec.z * cameraDistance + this.lookAt.z
        )

        this.viewMatrix.identity()
        this.viewMatrix.lookAt(cameraPosition, this.lookAt, orthoNormalize(this.upVector, this.lookVec))
    }

    fun setPosition(x: Float, y: Float, z: Float) {
        this.position.set(x, y, z)
    }

    fun setRotation(yaw: Float, pitch: Float) {
        this.yaw = yaw
        this.pitch = pitch
    }

    fun setLookAt(x: Float, y: Float, z: Float) {
        this.lookAt.set(x, y, z)
    }

    companion object {
        private val UP_VECTOR = Vector3f(0f, 1f, 0f)
    }
}