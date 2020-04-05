package com.harper.spacewar.main.gl.vbo

import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.buffer.BufferBuilder
import com.harper.spacewar.main.gl.buffer.VertexElement
import com.harper.spacewar.main.gl.buffer.VertexFormat
import org.lwjgl.BufferUtils
import java.nio.IntBuffer

class VertexBuffer {
    private var glVertexBuffer = GlUtils.glGenBuffers()
    private var bufferBuilder: BufferBuilder? = null
    private var indexBuffer: IntBuffer = BufferUtils.createIntBuffer(0)

    fun bufferData(bufferBuilder: BufferBuilder, indexBuffer: IntBuffer) {
        this.bufferBuilder = bufferBuilder
        this.indexBuffer = indexBuffer
        bindBuffer {
            GlUtils.glBufferData(bufferBuilder.rawByteBuffer, GlUtils.STATIC_DRAW)
        }
    }

    fun render() {
        bufferBuilder?.let { bb ->
            bindBuffer {
                iterateVertexElements(bb.vertexFormat) { index, vertexElement ->
                    when (vertexElement.type) {
                        VertexElement.Type.POSITION_3F -> {
                            GlUtils.glVertexAttribPointer(
                                0,
                                vertexElement.count,
                                vertexElement.format.glPointer,
                                false,
                                bb.vertexFormat.nextOffset,
                                bb.vertexFormat.getOffset(index).toLong()
                            )
                            GlUtils.glEnableVertexAttribArray(0)
                        }
                        VertexElement.Type.TEX_2F -> {
                            if (bb.vertexFormat.hasTextures) {
                                GlUtils.glVertexAttribPointer(
                                    1,
                                    vertexElement.count,
                                    vertexElement.format.glPointer,
                                    false,
                                    bb.vertexFormat.nextOffset,
                                    bb.vertexFormat.getOffset(index).toLong()
                                )
                                GlUtils.glEnableVertexAttribArray(1)
                            }
                        }
                        VertexElement.Type.NORMAL_3B -> {
                            if (bb.vertexFormat.hasNormals) {
                                GlUtils.glVertexAttribPointer(
                                    2,
                                    vertexElement.count,
                                    vertexElement.format.glPointer,
                                    true,
                                    bb.vertexFormat.nextOffset,
                                    bb.vertexFormat.getOffset(index).toLong()
                                )
                                GlUtils.glEnableVertexAttribArray(2)
                            }
                        }
                        else -> {
                            /** Do nothing, just pass **/
                        }
                    }
                }

                if (indexBuffer.capacity() > 0) {
                    GlUtils.glDrawElements(bb.drawMode, indexBuffer)
                } else GlUtils.glDrawArrays(bb.drawMode, 0, bb.vertexCount)

                iterateVertexElements(bb.vertexFormat) { _, vertexElement ->
                    when (vertexElement.type) {
                        VertexElement.Type.POSITION_3F -> {
                            GlUtils.glDisableVertexAttribArray(0)
                        }
                        VertexElement.Type.TEX_2F -> {
                            if (bb.vertexFormat.hasTextures)
                                GlUtils.glDisableVertexAttribArray(1)
                        }
                        VertexElement.Type.NORMAL_3B -> {
                            if (bb.vertexFormat.hasNormals)
                                GlUtils.glDisableVertexAttribArray(2)
                        }
                        else -> {
                            /** Do nothing, just pass **/
                        }
                    }
                }
            }
        } ?: throw IllegalStateException("Unable to render vertex buffer, it's data is empty")
    }

    fun deleteBuffers() {
        if (glVertexBuffer > 0) {
            GlUtils.glDeleteBuffers(glVertexBuffer)
            glVertexBuffer = -1
        }
    }

    private fun bindBuffer(binding: () -> Unit) {
        if (glVertexBuffer == -1)
            IllegalStateException("Unable to bind buffer, cause it's not allocated")
        GlUtils.glBindBuffer(GlUtils.ARRAY_BUFFER, glVertexBuffer)
        binding.invoke()
        GlUtils.glBindBuffer(GlUtils.ARRAY_BUFFER, 0)
    }

    private fun iterateVertexElements(vertexFormat: VertexFormat, consumer: (Int, VertexElement) -> Unit) {
        for (i in 0 until vertexFormat.elementCount)
            consumer.invoke(i, vertexFormat.getElement(i))
    }
}