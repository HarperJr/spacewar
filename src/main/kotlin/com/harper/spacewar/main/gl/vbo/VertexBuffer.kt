package com.harper.spacewar.main.gl.vbo

import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.buffer.VertexFormat

class VertexBuffer(private val vertexFormat: VertexFormat) {
    private var glBuffer = -1

    fun create() {
        if (glBuffer == -1)
            glBuffer = GlUtils.glGenBuffers()
    }

    fun bufferData() {

    }

    fun destroy() {
        if (glBuffer != -1)
            GlUtils.glDeleteBuffers(glBuffer)
    }
}