package com.harper.spacewar.main.mesh.material

import com.harper.spacewar.main.gl.texture.Texture
import org.joml.Vector4f

/**
 * This class represents materials instances
 */
class Material(
    val name: String,
    val ambient: Vector4f,
    val diffuse: Vector4f,
    val specular: Vector4f,
    val emissive: Vector4f,
    val texAmbient: Texture,
    val texDiffuse: Texture,
    val texSpecular: Texture
)