package com.harper.spacewar.main.mesh

import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.buffer.BufferBuilder
import com.harper.spacewar.main.gl.buffer.VertexElement
import com.harper.spacewar.main.gl.buffer.VertexFormat
import com.harper.spacewar.main.gl.vbo.VertexBuffer
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL13
import java.nio.IntBuffer

class MeshRenderer(private val mesh: Mesh) {
    private val vertexBuffer: VertexBuffer = VertexBuffer()
    private val bufferBuilder = BufferBuilder(0x100000)

    private var isBufferCreated = false

    fun render(vertexFormat: VertexFormat) {
        if (!isBufferCreated)
            createVertexBuffer(vertexFormat)
        vertexBuffer.render()
    }

    private fun createVertexBuffer(vertexFormat: VertexFormat) {
        bufferBuilder.create(GlUtils.DRAW_MODE_TRIANGLES, vertexFormat)
        mesh.render(bufferBuilder)
        vertexBuffer.bufferData(bufferBuilder, mesh.data.indexes)
        this.isBufferCreated = true
    }
}