package com.harper.spacewar.utils

import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.*

val PI: Float = Math.PI.toFloat()

fun vecFromEuler(yaw: Float, pitch: Float): Vector3f {
    val f = cos(toRadians(-yaw) - PI)
    val f1 = sin(toRadians(-yaw) - PI)
    val f2 = -cos(toRadians(-pitch))
    val f3 = sin(toRadians(-pitch))
    return Vector3f(f1 * f2, f3, f * f2).normalize()
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

fun lookRotation(lookAt: Vector3f, up: Vector3f): Quaternionf {
    val forward = Vector3f(0f, 0f, 1f)
    val dot = forward.dot(lookAt)

    if (abs(dot + 1.0f) < 0.000001f)
        return Quaternionf(up.x, up.y, up.z, PI)

    if (abs(dot - 1.0f) < 0.000001f)
        return Quaternionf(0f, 0f, 0f, 1f)

    val rotAngle = acos(dot)
    val rotAxis = forward.cross(lookAt, Vector3f()).normalize()

    val halfAngle = rotAngle * 0.5f
    val s = sin(halfAngle)
    return Quaternionf(
        rotAxis.x * s,
        rotAxis.y * s,
        rotAxis.z * s,
        cos(halfAngle)
    )
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