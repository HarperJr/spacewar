package com.harper.spacewar.main.gl.tessellator

import com.harper.spacewar.logging.Logger
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.buffer.BufferBuilder
import com.harper.spacewar.main.gl.buffer.VertexElement
import com.harper.spacewar.main.gl.buffer.VertexFormat
import org.lwjgl.opengl.GL11
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
                        GlUtils.glVertexPointer(
                            vertexElement.count,
                            vertexElement.format.glPointer,
                            vertexFormat.nextOffset,
                            vertexElementBuffer
                        )
                        GlUtils.glEnableClientState(GL11.GL_VERTEX_ARRAY)
                    }
                    VertexElement.Type.TEX_2F -> {
                        GlUtils.glTexCoordPointer(
                            vertexElement.count,
                            vertexElement.format.glPointer,
                            vertexFormat.nextOffset,
                            vertexElementBuffer
                        )
                        GlUtils.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY)
                    }
                    VertexElement.Type.COLOR_4B -> {
                        GlUtils.glColorPointer(
                            vertexElement.count,
                            vertexElement.format.glPointer,
                            vertexFormat.nextOffset,
                            vertexElementBuffer
                        )
                        GlUtils.glEnableClientState(GL11.GL_COLOR_ARRAY)
                    }
                    VertexElement.Type.NORMAL_3B -> {
                        GlUtils.glNormalPointer(
                            vertexElement.format.glPointer,
                            vertexFormat.nextOffset,
                            vertexElementBuffer
                        )
                        GlUtils.glEnableClientState(GL11.GL_NORMAL_ARRAY)
                    }
                    else -> return@iterateVertexElements
                }
            }

            GlUtils.glDrawArrays(this.bufferBuilder.drawMode, 0, this.bufferBuilder.vertexCount)

            iterateVertexElements(vertexFormat) { _, vertexElement ->
                when (vertexElement.type) {
                    VertexElement.Type.POSITION_3F -> {
                        GlUtils.glDisableClientState(GL11.GL_VERTEX_ARRAY)
                    }
                    VertexElement.Type.TEX_2F -> {
                        GlUtils.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY)
                    }
                    VertexElement.Type.COLOR_4B -> {
                        GlUtils.glDisableClientState(GL11.GL_COLOR_ARRAY)
                    }
                    VertexElement.Type.NORMAL_3B -> {
                        GlUtils.glDisableClientState(GL11.GL_NORMAL_ARRAY)
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