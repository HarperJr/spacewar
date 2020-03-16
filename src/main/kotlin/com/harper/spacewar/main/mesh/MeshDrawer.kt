package com.harper.spacewar.main.mesh

import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.buffer.BufferBuilder
import com.harper.spacewar.main.gl.buffer.VertexElement
import com.harper.spacewar.main.gl.buffer.VertexFormat
import com.harper.spacewar.main.gl.vbo.VertexBuffer
import org.lwjgl.BufferUtils

class MeshDrawer(private val mesh: Mesh, private val vertexFormat: VertexFormat) {
    private val vertexBuffers: VertexBuffer = VertexBuffer()
    private var indexBuffer = BufferUtils.createIntBuffer(0x10000)
    private var isBufferInflated = false

    fun draw(drawMode: Int) {
        if (!isBufferInflated)
            inflateMeshBuffer()

        vertexBuffers.bindBuffer {
            val elementsCount = vertexFormat.elementCount
            for (index in 0 until elementsCount) {
                val vertexElement = vertexFormat.getElement(index)
                when (vertexElement.type) {
                    VertexElement.Type.POSITION_3F -> {
                        GlUtils.glVertexPointer(
                            vertexElement.count,
                            vertexElement.format.glPointer,
                            vertexFormat.nextOffset,
                            vertexFormat.getOffset(index).toLong()
                        )
                    }
                    VertexElement.Type.TEX_2F -> {
                        GlUtils.glVertexPointer(
                            vertexElement.count,
                            vertexElement.format.glPointer,
                            vertexFormat.nextOffset,
                            vertexFormat.getOffset(index).toLong()
                        )
                    }
                    VertexElement.Type.NORMAL_3B -> {
                        GlUtils.glNormalPointer(
                            vertexElement.format.glPointer,
                            vertexFormat.nextOffset,
                            vertexFormat.getOffset(index).toLong()
                        )
                    }
                    else -> {
                        /** Do nothing, just pass **/
                    }
                }
            }

            GlUtils.glDrawElements(drawMode, indexBuffer)
        }
    }

    private fun inflateMeshBuffer() {
        val meshData = mesh.data
        val bufferBuilder = BufferBuilder(0x100000)
        bufferBuilder.create(GlUtils.DRAW_MODE_TRIANGLES, vertexFormat)

        val verticesCount = meshData.vertices.count()
        for (i in 0 until verticesCount) {
            bufferBuilder.pos(meshData.vertices[i * 3 + 0], meshData.vertices[i * 3 + 1], meshData.vertices[i * 3 + 2])
                .tex(meshData.texCoords[i * 2 + 0], meshData.texCoords[i * 2 + 1])
                .norm(meshData.normals[i * 3 + 0], meshData.normals[i * 3 + 1], meshData.normals[i * 3 + 2])
                .completeVertex()
        }

        val elementsCount = meshData.elements.count()
        if (indexBuffer.limit() < elementsCount)
            extendBuffer(elementsCount)

        with(indexBuffer) {
            put(meshData.elements)
            limit(meshData.elements.count())
            position(0)
        }

        vertexBuffers.bufferData(bufferBuilder)
        isBufferInflated = true
    }

    private fun extendBuffer(cap: Int) {
        val newIndexBuffer = BufferUtils.createIntBuffer(cap)
        newIndexBuffer.put(indexBuffer)
        indexBuffer.position(0)
    }
}