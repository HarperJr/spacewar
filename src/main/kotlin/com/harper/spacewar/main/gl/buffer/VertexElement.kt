package com.harper.spacewar.main.gl.buffer

import org.lwjgl.opengl.GL11

class VertexElement(val format: Format, val type: Type, val count: Int) {
    val usage: Int
        get() = format.usage * count

    fun isPositionElement(): Boolean {
        return type == Type.POSITION_3F
    }

    companion object {
        val POSITION_3F = VertexElement(Format.FLOAT, Type.POSITION_3F, 3)
        val TEX_2F = VertexElement(Format.FLOAT, Type.TEX_2F, 2)
        val NORMAL_3B = VertexElement(Format.BYTE, Type.NORMAL_3B, 3)
        val COLOR_4B = VertexElement(Format.UBYTE, Type.COLOR_4B, 4)
    }

    enum class Format(val usage: Int, val glPointer: Int) {
        UBYTE(1, GL11.GL_UNSIGNED_BYTE),
        BYTE(1, GL11.GL_BYTE),
        UINT(4, GL11.GL_UNSIGNED_INT),
        INT(4, GL11.GL_INT),
        FLOAT(4, GL11.GL_FLOAT),
        DOUBLE(8, GL11.GL_DOUBLE)
    }

    enum class Type {
        POSITION_3F,
        TEX_2F,
        NORMAL_3B,
        COLOR_4B,
        PADDING
    }
}
