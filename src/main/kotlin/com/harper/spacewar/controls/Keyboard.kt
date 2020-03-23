package com.harper.spacewar.controls

import com.harper.spacewar.display.Key
import com.harper.spacewar.display.listener.KeyboardListener
import org.lwjgl.BufferUtils

object Keyboard : KeyboardListener {
    var key: Key = Key.UNDEFINED
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

    fun event(): Keyboard.Event {
        if (this.eventBuffer.position() < this.eventIndex)
            return Keyboard.Event.UNDEFINED
        val eventOrdinal = eventBuffer.get(this.eventIndex)
        this.eventIndex = (this.eventIndex + 1) % eventBuffer.limit()
        return Keyboard.Event.values().find {
            it.ordinal == eventOrdinal
        } ?: Keyboard.Event.UNDEFINED
    }

    override fun onPressed(key: Key) {
        this.key = key
        putEvent(Event.PRESS)
    }

    override fun onReleased(key: Key) {
        this.key = key
        putEvent(Event.RELEASE)
    }

    private fun putEvent(event: Event) {
        if (this.eventBuffer.remaining() == 0)
            this.eventBuffer.position(0)
        this.eventBuffer.put(event.ordinal)
    }

    enum class Event {
        UNDEFINED, PRESS, RELEASE
    }
}
