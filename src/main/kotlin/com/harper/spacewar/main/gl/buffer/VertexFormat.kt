package com.harper.spacewar.main.gl.buffer

import com.harper.spacewar.logging.Logger

class VertexFormat(vararg vertexElements: VertexElement) {
    val elementCount: Int
        get() = elements.count()

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
            this.elements.add(element)
            this.offsets.add(this.nextOffset)

            this.nextOffset += element.usage
        }
    }

    private fun hasPosition(): Boolean {
        return elements.find { it.type == VertexElement.Type.POSITION_3F } != null
    }

    companion object {
        val POSITION = VertexFormat(VertexElement.POSITION_3F)

        val POSITION_COLOR = VertexFormat(
            VertexElement.POSITION_3F,
            VertexElement.COLOR_4B
        )

        val POSITION_TEX_COLOR = VertexFormat(
            VertexElement.POSITION_3F,
            VertexElement.TEX_2F,
            VertexElement.COLOR_4B
        )

        val POSITION_TEX = VertexFormat(
            VertexElement.POSITION_3F,
            VertexElement.TEX_2F
        )

        val OPAQUE_MODEL = VertexFormat(
            VertexElement.POSITION_3F,
            VertexElement.TEX_2F,
            VertexElement.NORMAL_3B
        )

        val OPAQUE_MODEL_UNTEXTURED = VertexFormat(
            VertexElement.POSITION_3F,
            VertexElement.NORMAL_3B
        )
    }
}