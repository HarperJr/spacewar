package com.harper.spacewar.display.listener

interface MouseListener {
    fun onClicked(x: Float, y: Float)

    fun onPressed(x: Float, y: Float)

    fun onMoved(x: Float, y: Float)

    fun onScrolled(x: Float, y: Float)
}