package com.harper.spacewar.main.gl.texture

data class Texture(val width: Int, val height: Int, val glTexture: Int, val type: Type) {
    enum class Type {
        AMBIENT,
        DIFFUSE,
        SPECULAR,
        NORMAL
    }
}