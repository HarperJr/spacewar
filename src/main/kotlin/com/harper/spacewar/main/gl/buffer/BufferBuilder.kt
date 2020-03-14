package com.harper.spacewar.main.gl.buffer

import org.lwjgl.BufferUtils
import java.nio.ByteBuffer

class BufferBuilder(capacity: Int) {
    var vertexCount: Int = 0
        private set

    private val rawByteBuffer: ByteBuffer = BufferUtils.createByteBuffer(capacity)
    private val rawIntBuffer = rawByteBuffer.asIntBuffer()
    private val rawFloatBuffer = rawByteBuffer.asFloatBuffer()

    private val vertexFormatElement: VertexElement
        get() = vertexFormat.getElement(vertexFormatIndex)

    private var vertexFormat: VertexFormat = VertexFormat.POSITION_TEX
    private var vertexFormatIndex = 0

    private var xOffset = 0f
    private var yOffset = 0f
    private var zOffset = 0f

    fun tesselate(vertexFormat: VertexFormat, tess: BufferBuilder.() -> Unit) {
        this.vertexFormat = vertexFormat
        this.vertexFormatIndex = 0

        tess.invoke(this)

        flush()
    }

    fun offset(x: Float, y: Float, z: Float) {
        xOffset = x
        yOffset = y
        zOffset = z
    }

    fun pos(x: Float, y: Float, z: Float): BufferBuilder = this.apply {
        val index = vertexCount * vertexFormat.nextOffset + this.vertexFormat.getOffset(this.vertexFormatIndex)
        when (this.vertexFormatElement.format) {
            VertexElement.Format.FLOAT -> {
                rawByteBuffer.putFloat(index + 0 * 4, x + xOffset)
                rawByteBuffer.putFloat(index + 1 * 4, y + yOffset)
                rawByteBuffer.putFloat(index + 2 * 4, z + zOffset)
            }
            VertexElement.Format.UINT, VertexElement.Format.INT -> {
                rawByteBuffer.putInt(index + 0 * 4, (x + xOffset).toInt())
                rawByteBuffer.putInt(index + 1 * 4, (y + yOffset).toInt())
                rawByteBuffer.putInt(index + 2 * 4, (z + zOffset).toInt())
            }
            VertexElement.Format.UBYTE, VertexElement.Format.BYTE -> {
                rawByteBuffer.put(index + 0, (x + xOffset).toByte())
                rawByteBuffer.put(index + 1, (y + yOffset).toByte())
                rawByteBuffer.put(index + 2, (z + zOffset).toByte())
            }
        }

        this.nextVertexFormatIndex()
    }

    fun tex(u: Float, v: Float): BufferBuilder = this.apply {
        val index = vertexCount * vertexFormat.nextOffset + vertexFormat.getOffset(vertexFormatIndex)
        when (vertexFormatElement.format) {
            VertexElement.Format.FLOAT -> {
                rawByteBuffer.putFloat(index + 0, u)
                rawByteBuffer.putFloat(index + 1, v)
            }
            else -> return this
        }
        nextVertexFormatIndex()
    }

    fun norm(x: Float, y: Float, z: Float): BufferBuilder = this.apply {
        val index = vertexCount * vertexFormat.nextOffset + this.vertexFormat.getOffset(this.vertexFormatIndex)
        when (this.vertexFormatElement.format) {
            VertexElement.Format.FLOAT -> {
                rawByteBuffer.putFloat(index + 0 * 4, x)
                rawByteBuffer.putFloat(index + 1 * 4, y)
                rawByteBuffer.putFloat(index + 2 * 4, z)
            }
            VertexElement.Format.UINT, VertexElement.Format.INT -> {
                rawByteBuffer.putInt(index + 0 * 4, x.toInt())
                rawByteBuffer.putInt(index + 1 * 4, y.toInt())
                rawByteBuffer.putInt(index + 2 * 4, z.toInt())
            }
            VertexElement.Format.UBYTE, VertexElement.Format.BYTE -> {
                rawByteBuffer.put(index + 0, (x.toInt() * 127 and 255).toByte())
                rawByteBuffer.put(index + 1, (y.toInt() * 127 and 255).toByte())
                rawByteBuffer.put(index + 2, (z.toInt() * 127 and 255).toByte())
            }
            else -> return this
        }
        this.nextVertexFormatIndex()
    }

    fun completeVertex() {
        vertexCount++
    }

    fun flush() {

    }

    private fun nextVertexFormatIndex() {
        ++this.vertexFormatIndex
        this.vertexFormatIndex %= this.vertexFormat.elementCount

        if (this.vertexFormatElement.type === VertexElement.Type.PADDING) {
            this.nextVertexFormatIndex()
        }
    }
}