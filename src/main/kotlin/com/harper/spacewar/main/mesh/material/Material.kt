package com.harper.spacewar.main.mesh.material

import com.harper.spacewar.main.gl.texture.Texture
import org.joml.Vector4f

/**
 * This class represents materials instances
 */
class Material(val name: String, val colors: List<Color>, val textures: List<Texture>)