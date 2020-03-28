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

    private var indexBuffer: IntBuffer = BufferUtils.createIntBuffer(0)
    private var isBufferCreated = false

    fun render(vertexFormat: VertexFormat) {
        if (!isBufferCreated)
            createVertexBuffer(vertexFormat)
        val mData = this.mesh.data
        vertexBuffer.bindBuffer {
            iterateVertexElements(vertexFormat) { index, vertexElement ->
                when (vertexElement.type) {
                    VertexElement.Type.POSITION_3F -> {
                        GlUtils.glVertexAttribPointer(
                            0,
                            vertexElement.count,
                            vertexElement.format.glPointer,
                            false,
                            vertexFormat.nextOffset,
                            vertexFormat.getOffset(index).toLong()
                        )
                        GlUtils.glEnableVertexAttribArray(0)
                    }
                    VertexElement.Type.TEX_2F -> {
                        if (mData.hasTextures) {
                            GlUtils.glVertexAttribPointer(
                                1,
                                vertexElement.count,
                                vertexElement.format.glPointer,
                                false,
                                vertexFormat.nextOffset,
                                vertexFormat.getOffset(index).toLong()
                            )
                            GlUtils.glEnableVertexAttribArray(1)
                            GlUtils.glClientActiveTexture(GL13.GL_TEXTURE0)
                        }
                    }
                    VertexElement.Type.NORMAL_3B -> {
                        if (mData.hasNormals) {
                            GlUtils.glVertexAttribPointer(
                                2,
                                vertexElement.count,
                                vertexElement.format.glPointer,
                                true,
                                vertexFormat.nextOffset,
                                vertexFormat.getOffset(index).toLong()
                            )
                            GlUtils.glEnableVertexAttribArray(2)
                        }
                    }
                    else -> {
                        /** Do nothing, just pass **/
                    }
                }
            }

            GlUtils.glDrawElements(bufferBuilder.drawMode, indexBuffer)

            iterateVertexElements(vertexFormat) { _, vertexElement ->
                when (vertexElement.type) {
                    VertexElement.Type.POSITION_3F -> {
                        GlUtils.glDisableVertexAttribArray(0)
                    }
                    VertexElement.Type.TEX_2F -> {
                        if (mData.hasTextures)
                            GlUtils.glDisableVertexAttribArray(1)
                    }
                    VertexElement.Type.NORMAL_3B -> {
                        if (mData.hasNormals)
                            GlUtils.glDisableVertexAttribArray(2)
                    }
                    else -> {
                        /** Do nothing, just pass **/
                    }
                }
            }
        }
    }

    private fun iterateVertexElements(vertexFormat: VertexFormat, consumer: (Int, VertexElement) -> Unit) {
        for (i in 0 until vertexFormat.elementCount)
            consumer.invoke(i, vertexFormat.getElement(i))
    }

    private fun createVertexBuffer(vertexFormat: VertexFormat) {
        bufferBuilder.create(GlUtils.DRAW_MODE_TRIANGLES, vertexFormat)
        mesh.render(bufferBuilder)
        vertexBuffer.bufferData(bufferBuilder)
        this.indexBuffer = mesh.data.indexes
        this.isBufferCreated = true
    }
}