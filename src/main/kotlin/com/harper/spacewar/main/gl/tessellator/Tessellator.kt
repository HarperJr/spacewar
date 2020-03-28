package com.harper.spacewar.main.gl.tessellator

import com.harper.spacewar.logging.Logger
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.buffer.BufferBuilder
import com.harper.spacewar.main.gl.buffer.VertexElement
import com.harper.spacewar.main.gl.buffer.VertexFormat
import org.lwjgl.opengl.GL13
import java.nio.ByteBuffer

class Tessellator(bufferCapacity: Int) {
    private val logger = Logger.getLogger<Tessellator>()
    private val bufferBuilder = BufferBuilder(bufferCapacity)

    fun tessellate(
        drawMode: Int = GlUtils.DRAW_MODE_TRIANGLES,
        vertexFormat: VertexFormat,
        tessellation: BufferBuilder.() -> Unit
    ) {
        this.bufferBuilder.create(drawMode, vertexFormat)
        tessellation.invoke(this.bufferBuilder)

        val canBeTessellated = this.bufferBuilder.vertexCount > 0
        if (canBeTessellated) {
            iterateVertexElements(vertexFormat) { index, vertexElement ->
                val vertexElementBuffer =
                    this.bufferBuilder.rawByteBuffer.position(vertexFormat.getOffset(index)) as ByteBuffer
                when (vertexElement.type) {
                    VertexElement.Type.POSITION_3F -> {
                        GlUtils.glVertexAttribPointer(
                            0,
                            vertexElement.count,
                            vertexElement.format.glPointer,
                            false,
                            vertexFormat.nextOffset,
                            vertexElementBuffer
                        )
                        GlUtils.glEnableVertexAttribArray(0)
                    }
                    VertexElement.Type.TEX_2F -> {
                        GlUtils.glVertexAttribPointer(
                            1,
                            vertexElement.count,
                            vertexElement.format.glPointer,
                            false,
                            vertexFormat.nextOffset,
                            vertexElementBuffer
                        )
                        GlUtils.glEnableVertexAttribArray(1)
                        GlUtils.glClientActiveTexture(GL13.GL_TEXTURE0)
                    }
                    VertexElement.Type.NORMAL_3B -> {
                        GlUtils.glVertexAttribPointer(
                            2,
                            vertexElement.count,
                            vertexElement.format.glPointer,
                            true,
                            vertexFormat.nextOffset,
                            vertexElementBuffer
                        )
                        GlUtils.glEnableVertexAttribArray(2)
                    }
                    VertexElement.Type.COLOR_4B -> {
                        GlUtils.glVertexAttribPointer(
                            3,
                            vertexElement.count,
                            vertexElement.format.glPointer,
                            true,
                            vertexFormat.nextOffset,
                            vertexElementBuffer
                        )
                        GlUtils.glEnableVertexAttribArray(3)
                    }
                    else -> return@iterateVertexElements
                }
            }

            GlUtils.glDrawArrays(this.bufferBuilder.drawMode, 0, this.bufferBuilder.vertexCount)

            iterateVertexElements(vertexFormat) { _, vertexElement ->
                when (vertexElement.type) {
                    VertexElement.Type.POSITION_3F -> {
                        GlUtils.glDisableVertexAttribArray(0)
                    }
                    VertexElement.Type.TEX_2F -> {
                        GlUtils.glDisableVertexAttribArray(1)
                    }
                    VertexElement.Type.NORMAL_3B -> {
                        GlUtils.glDisableVertexAttribArray(2)
                    }
                    VertexElement.Type.COLOR_4B -> {
                        GlUtils.glDisableVertexAttribArray(3)
                    }
                    else -> return@iterateVertexElements
                }
            }
        }

        this.bufferBuilder.resetBuffer()
    }

    private fun iterateVertexElements(vertexFormat: VertexFormat, consumer: (Int, VertexElement) -> Unit) {
        for (index in 0 until vertexFormat.elementCount) {
            consumer.invoke(index, vertexFormat.getElement(index))
        }
    }

    companion object {
        private var tessellatorInstance: Tessellator? = null
        val instance: Tessellator
            get() {
                return synchronized(this) {
                    if (tessellatorInstance == null)
                        tessellatorInstance = Tessellator(0x10000)
                    tessellatorInstance!!
                }
            }
    }
}