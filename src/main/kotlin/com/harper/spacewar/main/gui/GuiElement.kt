package com.harper.spacewar.main.gui

interface GuiElement {
    val xPos: Float
    val yPos: Float

    fun render(gui: Gui)
}