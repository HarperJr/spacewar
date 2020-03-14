package com.harper.spacewar.main.gl.buffer

class VertexFormat(private vararg val vertexElements: VertexElement) {
    val elementCount: Int
        get() = elements.count()

    var texCoordOffset: Int = -1
        private set

    var normalOffset: Int = -1
        private set

    var elementOffset: Int = -1
        private set

    var nextOffset = -1
        private set

    private val elements: MutableList<VertexElement> = mutableListOf()
    private val offsets: MutableList<Int> = mutableListOf()

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

        } else {
            when (element.type) {
                VertexElement.Type.TEX_2F -> texCoordOffset = nextOffset
                VertexElement.Type.NORMAL_3B -> normalOffset = nextOffset
                VertexElement.Type.ELEMENT_1I -> elementOffset = nextOffset
                else -> return
            }
            elements.add(element)
            offsets.add(nextOffset)

            nextOffset += element.size
        }
    }

    private fun hasPosition(): Boolean {
        return vertexElements.find { it.type == VertexElement.Type.POSITION_3F } != null
    }

    companion object {
        val POSITION_TEX = VertexFormat(VertexElement.POSITION_3F, VertexElement.TEX_2F)
    }
}