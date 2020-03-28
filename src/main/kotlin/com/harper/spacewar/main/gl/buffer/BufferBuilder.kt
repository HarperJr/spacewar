package com.harper.spacewar.main.gl.buffer

import org.lwjgl.BufferUtils
import java.nio.ByteBuffer
import java.nio.IntBuffer
import kotlin.math.ceil

class BufferBuilder(capacity: Int) {
    var vertexCount: Int = 0
        private set

    var drawMode: Int = 0
        private set

    var vertexFormat: VertexFormat = VertexFormat.POSITION_TEX
        private set

    var rawByteBuffer: ByteBuffer = BufferUtils.createByteBuffer(capacity)
        private set

    var rawIntBuffer: IntBuffer = rawByteBuffer.asIntBuffer()
        private set

    private val vertexFormatElement: VertexElement
        get() = vertexFormat.getElement(vertexFormatIndex)

    private var vertexFormatIndex = 0

    private var xOffset = 0f
    private var yOffset = 0f
    private var zOffset = 0f

    fun create(drawMode: Int, vertexFormat: VertexFormat) {
        this.drawMode = drawMode
        this.vertexFormat = vertexFormat
    }

    fun offset(x: Float, y: Float, z: Float) {
        xOffset = x
        yOffset = y
        zOffset = z
    }

    fun pos(x: Float, y: Float, z: Float): BufferBuilder = this.apply {
        val index =
            this.vertexCount * this.vertexFormat.nextOffset + this.vertexFormat.getOffset(this.vertexFormatIndex)
        when (this.vertexFormatElement.format) {
            VertexElement.Format.FLOAT -> {
                rawByteBuffer.putFloat(index + 0, x + xOffset)
                rawByteBuffer.putFloat(index + 4, y + yOffset)
                rawByteBuffer.putFloat(index + 8, z + zOffset)
            }
            VertexElement.Format.UINT, VertexElement.Format.INT -> {
                rawByteBuffer.putInt(index + 0, (x + xOffset).toInt())
                rawByteBuffer.putInt(index + 4, (y + yOffset).toInt())
                rawByteBuffer.putInt(index + 8, (z + zOffset).toInt())
            }
            VertexElement.Format.UBYTE, VertexElement.Format.BYTE -> {
                rawByteBuffer.put(index + 0, (x + xOffset).toByte())
                rawByteBuffer.put(index + 1, (y + yOffset).toByte())
                rawByteBuffer.put(index + 2, (z + zOffset).toByte())
            }
            else -> return this
        }

        this.nextVertexFormatIndex()
    }

    fun color(color: Long): BufferBuilder = this.apply {
        val index =
            this.vertexCount * this.vertexFormat.nextOffset + this.vertexFormat.getOffset(this.vertexFormatIndex)
        when (vertexFormatElement.format) {
            VertexElement.Format.UBYTE -> {
                rawByteBuffer.put(index + 0, (color shr 8 and 255).toByte())
                rawByteBuffer.put(index + 1, (color shr 16 and 255).toByte())
                rawByteBuffer.put(index + 2, (color shr 24 and 255).toByte())
                rawByteBuffer.put(index + 3, (color and 255).toByte())
            }
            else -> return this
        }
        nextVertexFormatIndex()
    }

    fun tex(u: Float, v: Float): BufferBuilder = this.apply {
        val index =
            this.vertexCount * this.vertexFormat.nextOffset + this.vertexFormat.getOffset(this.vertexFormatIndex)
        when (vertexFormatElement.format) {
            VertexElement.Format.FLOAT -> {
                rawByteBuffer.putFloat(index + 0, u)
                rawByteBuffer.putFloat(index + 4, v)
            }
            else -> return this
        }
        nextVertexFormatIndex()
    }

    fun norm(x: Float, y: Float, z: Float): BufferBuilder = this.apply {
        val index =
            this.vertexCount * this.vertexFormat.nextOffset + this.vertexFormat.getOffset(this.vertexFormatIndex)
        when (this.vertexFormatElement.format) {
            VertexElement.Format.FLOAT -> {
                rawByteBuffer.putFloat(index + 0, x)
                rawByteBuffer.putFloat(index + 4, y)
                rawByteBuffer.putFloat(index + 8, z)
            }
            VertexElement.Format.UINT, VertexElement.Format.INT -> {
                rawByteBuffer.putInt(index + 0, x.toInt())
                rawByteBuffer.putInt(index + 4, y.toInt())
                rawByteBuffer.putInt(index + 8, z.toInt())
            }
            VertexElement.Format.UBYTE, VertexElement.Format.BYTE -> {
                rawByteBuffer.put(index + 0, (ceil(x * 255).toInt() and 255).toByte())
                rawByteBuffer.put(index + 1, (ceil(y * 255).toInt() and 255).toByte())
                rawByteBuffer.put(index + 2, (ceil(z * 255).toInt() and 255).toByte())
            }
            else -> return this
        }
        this.nextVertexFormatIndex()
    }

    fun completeVertex() {
        this.vertexCount++
        this.growBuffer(this.vertexFormat.nextOffset)
    }

    fun resetBuffer() {
        this.rawByteBuffer.limit(this.rawByteBuffer.capacity())
        this.rawByteBuffer.position(0)
        this.vertexFormatIndex = 0
        this.vertexCount = 0
    }

    private fun growBuffer(nextOffset: Int) {
        if (roundUp(nextOffset, 4) / 4 > this.rawIntBuffer.remaining() ||
            this.vertexCount * this.vertexFormat.nextOffset + nextOffset > this.rawByteBuffer.capacity()
        ) {
            val currentBufferCap = this.rawByteBuffer.capacity()
            val growBufferCap = currentBufferCap + roundUp(nextOffset, 2097152)
            val intBufferPos = this.rawIntBuffer.position()
            val byteBuffer = BufferUtils.createByteBuffer(growBufferCap)
            this.rawByteBuffer.position(0)
            byteBuffer.put(this.rawByteBuffer)
            byteBuffer.rewind()
            this.rawByteBuffer = byteBuffer
            this.rawIntBuffer = this.rawByteBuffer.asIntBuffer()
            this.rawIntBuffer.position(intBufferPos)
        }
    }

    private fun nextVertexFormatIndex() {
        this.vertexFormatIndex = (this.vertexFormatIndex + 1) % this.vertexFormat.elementCount
        if (this.vertexFormatElement.type == VertexElement.Type.PADDING)
            this.nextVertexFormatIndex()
    }

    private fun roundUp(number: Int, interval: Int): Int {
        var intValue = interval
        return if (intValue == 0) {
            0
        } else if (number == 0) {
            intValue
        } else {
            if (number < 0) {
                intValue *= -1
            }

            val i = number % intValue
            if (i == 0) number else number + intValue - i
        }
    }
}