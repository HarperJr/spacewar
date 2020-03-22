package com.harper.spacewar.controls

import com.harper.spacewar.display.listener.MouseListener
import org.lwjgl.BufferUtils

object Mouse : MouseListener {
    var xPos: Float = 0f
        private set
    var yPos: Float = 0f
        private set

    private var eventBuffer = BufferUtils.createIntBuffer(256)
    private var eventIndex = 0

    fun next(): Boolean {
        if (!this.eventBuffer.hasRemaining()) {
            this.eventBuffer.position(0)
            this.eventIndex = 0
        }
        return this.eventIndex < this.eventBuffer.position()
    }

    fun event(): Event {
        if (this.eventBuffer.position() < this.eventIndex)
            return Event.UNDEFINED
        val eventOrdinal = eventBuffer.get(this.eventIndex)
        this.eventIndex = (this.eventIndex + 1) % eventBuffer.limit()
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

    private fun putEvent(event: Event) {
        if (this.eventBuffer.remaining() == 0)
            this.eventBuffer.position(0)
        this.eventBuffer.put(event.ordinal)
    }

    enum class Event {
        UNDEFINED, CLICK, MOVE, PRESS
    }
}