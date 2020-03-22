package com.harper.spacewar

import org.joml.Matrix4f

interface CameraSource {
    val projection: Matrix4f
    val view: Matrix4f

    fun update(time: Float)

    fun setPosition(x: Float, y: Float, z: Float)

    fun setRotation(pitch: Float, yaw: Float, roll: Float)
}