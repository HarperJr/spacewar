package com.harper.spacewar.main.mesh.material

import com.harper.spacewar.main.gl.texture.Texture

/**
 * This class represents materials instances
 */
class Material(val name: String, val colors: List<Color>, val textures: List<MaterialTexture>) {
    val hasTextures: Boolean
        get() = this.textures.isNotEmpty()

    class MaterialTexture(val path: String, val type: Texture.Type)
}