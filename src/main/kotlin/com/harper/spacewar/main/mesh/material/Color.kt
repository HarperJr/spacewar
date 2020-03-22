package com.harper.spacewar.main.mesh.material

import org.joml.Vector4f

data class Color(val type: Type, val value: Vector4f) {
    enum class Type {
        AMBIENT,
        DIFFUSE,
        SPECULAR,
        EMISSIVE,
    }
}