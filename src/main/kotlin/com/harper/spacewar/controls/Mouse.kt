package com.harper.spacewar.controls

import com.harper.spacewar.display.listener.MouseListener
import org.lwjgl.BufferUtils

object Mouse : MouseListener {
    var xPos: Float = 0f
        private set

    var yPos: Float = 0f
        private set

    var scrollX: Float = 0f
        private set

    var scrollY: Float = 0f
        private set

    private var eventBuffer = BufferUtils.createIntBuffer(256)
    private var eventIndex = 0

    fun next(): Boolean {
        tryResetBuffer()
        return this.eventIndex < this.eventBuffer.position()
    }

    fun event(): Event {
        if (this.eventBuffer.position() < this.eventIndex)
            return Event.UNDEFINED
        val eventOrdinal = eventBuffer.get(this.eventIndex)
        this.eventIndex = (this.eventIndex + 1) % eventBuffer.capacity()
        return Event.values().find {
            it.ordinal == eventOrdinal
        } ?: Event.UNDEFINED
    }

    override fun onClicked(x: Float, y: Float) {
        this.xPos = x
        this.yPos = y
        putEvent(Event.CLICK)
    }

    override fun onPressed(x: Float, y: Float) {
        this.xPos = x
        this.yPos = y
        putEvent(Event.PRESS)
    }

    override fun onMoved(x: Float, y: Float) {
        this.xPos = x
        this.yPos = y
        putEvent(Event.MOVE)
    }

    override fun onScrolled(x: Float, y: Float) {
        this.scrollX = x
        this.scrollY = y
        putEvent(Event.SCROLL)
    }

    private fun putEvent(event: Event) {
        tryResetBuffer()
        this.eventBuffer.put(event.ordinal)
    }

    private fun tryResetBuffer() {
        if (!this.eventBuffer.hasRemaining()) {
            this.eventBuffer.position(0)
            this.eventIndex = 0
        }
    }

    enum class Event {
        UNDEFINED, CLICK, MOVE, PRESS, SCROLL
    }
}