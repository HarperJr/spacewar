package com.harper.spacewar.controls

import com.harper.spacewar.display.listener.MouseListener

object Mouse : MouseListener {
    var xPos: Float = 0f
        private set
    var yPos: Float = 0f
        private set
    var isClicked: Boolean = false
        get() {
            val clicked = field
            if (clicked)
                field = !clicked
            return clicked
        }
        private set

    override fun onClicked(x: Float, y: Float) {
        this.xPos = x
        this.yPos = y
        this.isClicked = true
    }

    override fun onPressed(x: Float, y: Float) {
        this.xPos = x
        this.yPos = y
    }

    override fun onMoved(x: Float, y: Float) {
        this.xPos = x
        this.yPos = y
    }
}
