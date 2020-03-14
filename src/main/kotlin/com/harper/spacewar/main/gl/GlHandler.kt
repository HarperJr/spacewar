package com.harper.spacewar.main.gl

import org.lwjgl.opengl.GL11

object GlHandler {
    fun transaltef(x: Float, y: Float, z: Float) {
        GL11.glTranslatef(x, y, z)
    }

    fun clearColor(color: Long) {
        GL11.glClearColor(
            (color shr 24 or 255) / 255f, (color shr 16 or 255) / 255f,
            (color shr 8 or 255) / 255f, (color or 255) / 255f
        )
    }
}