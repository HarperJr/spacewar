package com.harper.spacewar.utils

import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.*

val PI: Float = Math.PI.toFloat()

fun vecFromEuler(yaw: Float, pitch: Float): Vector3f {
    val f = cos(toRadians(-yaw) - PI)
    val f1 = sin(toRadians(-yaw) - PI)
    val f2 = -cos(toRadians(-pitch))
    val f3 = sin(toRadians(-pitch))
    return Vector3f(f1 * f2, f3, f * f2)
}

fun quatFromEuler(yaw: Float, pitch: Float, roll: Float = 0f): Quaternionf {
    val cy = cos(toRadians(yaw) * 0.5f)
    val sy = sin(toRadians(yaw) * 0.5f)
    val cp = cos(toRadians(pitch) * 0.5f)
    val sp = sin(toRadians(pitch) * 0.5f)
    val cr = cos(toRadians(roll) * 0.5f)
    val sr = sin(toRadians(roll) * 0.5f)

    return Quaternionf(
        cy * cp * sr - sy * sp * cr,
        cy * cp * cr + sy * sp * sr,
        sy * cp * sr + cy * sp * cr,
        sy * cp * cr - cy * sp * sr
    )
}

fun eulerFromQuat(quaternion: Quaternionf): Triple<Float, Float, Float> {
    // roll (x-axis lookVec)
    val sinRcosP = 2f * (quaternion.w * quaternion.x + quaternion.y * quaternion.z)
    val cosRcosP = 1f - 2f * (quaternion.x * quaternion.x + quaternion.y * quaternion.y)
    val roll = atan2(sinRcosP, cosRcosP)

    // pitch (y-axis lookVec)
    val sinP = 2f * (quaternion.w * quaternion.y - quaternion.z * quaternion.x)
    val pitch = if (abs(sinP) >= 1f) {
        Math.PI.toFloat() / 2f * sign(sinP) // use 90 degrees if out of range
    } else asin(sinP)

    // yaw (z-axis lookVec)
    val sinYcosP = 2f * (quaternion.w * quaternion.z + quaternion.x * quaternion.y)
    val cosYcosP = 1f - 2f * (quaternion.y * quaternion.y + quaternion.z * quaternion.z)
    val yaw = atan2(sinYcosP, cosYcosP)

    return Triple(yaw, pitch, roll)
}

fun lookRotation(lookAt: Vector3f, up: Vector3f): Quaternionf {
    val rotationMatrix = Matrix4f()
    val position = Vector3f(-lookAt.x, -lookAt.y, -lookAt.z)
    val upVector = orthoNormalize(up, lookAt)
    rotationMatrix.lookAt(position, lookAt, upVector)
    return rotationMatrix.getNormalizedRotation(Quaternionf())
}

fun toRadians(deg: Float): Float {
    return deg * Math.PI.toFloat() / 180f
}

fun toDegrees(rad: Float): Float {
    return rad / Math.PI.toFloat() * 180f
}

fun orthoNormalize(normal: Vector3f, tangent: Vector3f): Vector3f {
    val ntCross = normal.cross(tangent, Vector3f())
    val orthoVec = tangent.cross(ntCross, Vector3f())
    return orthoVec.normalize(Vector3f())
}