package com.harper.spacewar.main.gl.buffer

import com.harper.spacewar.logging.Logger

class VertexFormat(vararg vertexElements: VertexElement) {
    val elementCount: Int
        get() = elements.count()

    var texCoordOffset: Int = -1
        private set

    var normalOffset: Int = -1
        private set

    var elementOffset: Int = -1
        private set

    var nextOffset = 0
        private set

    private val elements: MutableList<VertexElement> = mutableListOf()
    private val offsets: MutableList<Int> = mutableListOf()

    private val logger = Logger.getLogger<VertexFormat>()

    init {
        vertexElements.forEach { element ->
            addElement(element)
        }
    }

    fun getElement(index: Int): VertexElement {
        return elements[index]
    }

    fun getOffset(index: Int): Int {
        return offsets[index]
    }

    private fun addElement(element: VertexElement) {
        if (element.isPositionElement() && hasPosition()) {
            logger.error(Throwable("This format already has position element"))
        } else {
            when (element.type) {
                VertexElement.Type.TEX_2F -> texCoordOffset = nextOffset
                VertexElement.Type.NORMAL_3B -> normalOffset = nextOffset
                else -> { /** Do nothing, just skip **/ }
            }
            elements.add(element)
            offsets.add(nextOffset)

            nextOffset += element.usage
        }
    }

    private fun hasPosition(): Boolean {
        return elements.find { it.type == VertexElement.Type.POSITION_3F } != null
    }

    companion object {
        val POSITION = VertexFormat(VertexElement.POSITION_3F)
        val POSITION_TEX = VertexFormat(
            VertexElement.POSITION_3F,
            VertexElement.TEX_2F
        )
        val OPAQUE_MODEL = VertexFormat(
            VertexElement.POSITION_3F,
            VertexElement.TEX_2F,
            VertexElement.NORMAL_3B
        )
    }
}