package com.harper.spacewar.main.gl.vbo

import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.buffer.BufferBuilder
import com.harper.spacewar.main.gl.buffer.VertexFormat

class VertexBuffer() {
    private var glVertexBuffer = GlUtils.glGenBuffers()
    private var vertexFormat = VertexFormat.POSITION

    fun bufferData(bufferBuilder: BufferBuilder) {
        this.vertexFormat = bufferBuilder.vertexFormat
        bindBuffer {
            GlUtils.glBufferData(bufferBuilder.rawByteBuffer, GlUtils.STATIC_DRAW)
        }
    }

    fun bindBuffer(binding: () -> Unit) {
        if (glVertexBuffer == -1)
            IllegalStateException("Unable to bind buffer, cause it's not allocated")
        GlUtils.glBindBuffer(GlUtils.ARRAY_BUFFER, glVertexBuffer)
        binding.invoke()
        GlUtils.glBindBuffer(GlUtils.ARRAY_BUFFER, 0)
    }

    fun deleteBuffers() {
        if (glVertexBuffer > 0) {
            GlUtils.glDeleteBuffers(glVertexBuffer)
            glVertexBuffer = -1
        }
    }
}