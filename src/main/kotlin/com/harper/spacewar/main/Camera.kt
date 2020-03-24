package com.harper.spacewar.main

import com.harper.spacewar.logging.Logger
import com.harper.spacewar.main.scene.Scene
import org.joml.Matrix4f
import org.joml.Vector3f
import kotlin.math.cos
import kotlin.math.sin

class Camera(private val scene: Scene, private val fov: Float) {
    private val logger: Logger = Logger.getLogger<Camera>()

    val projection: Matrix4f
        get() = projectionMatrix

    val view: Matrix4f
        get() = viewMatrix

    val lookVec: Vector3f
        get() {
            val f = cos(-yaw / 180f * Math.PI - Math.PI)
            val f1 = sin(-yaw / 180f * Math.PI - Math.PI)
            val f2 = -cos(-pitch / 180f * Math.PI)
            val f3 = sin(-pitch / 180f * Math.PI)
            return Vector3f((f1 * f2).toFloat(), f3.toFloat(), (f * f2).toFloat())
        }

    var position: Vector3f = Vector3f(0f)
        private set

    private val ratio: Float
        get() {
            return scene.scaledResolution.scaledWidth /
                    scene.scaledResolution.scaledHeight
        }

    private val lookAt: Vector3f = Vector3f()

    private var projectionMatrix = Matrix4f()
    private var viewMatrix = Matrix4f()

    private var pitch: Float = 0f
    private var yaw: Float = 0f

    fun update(time: Float) {
        this.projectionMatrix.identity()
        this.projectionMatrix.setPerspective(fov, ratio, 0.1f, 1000000f)

        val cameraDistance = this.position.distance(this.lookAt)
        val cameraPosition = Vector3f(
            -lookVec.x * cameraDistance + this.lookAt.x,
            -lookVec.y * cameraDistance + this.lookAt.y,
            -lookVec.z * cameraDistance + this.lookAt.z
        )

        logger.debug("Camera pos: $cameraPosition")

        this.viewMatrix.identity()
        this.viewMatrix.lookAt(cameraPosition, this.lookAt, UP_VECTOR)
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