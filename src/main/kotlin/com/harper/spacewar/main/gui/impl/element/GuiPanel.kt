package com.harper.spacewar.main.gui.impl.element

import com.harper.spacewar.main.gui.Gui
import com.harper.spacewar.main.gui.GuiElement

class GuiPanel(
    override val xPos: Float,
    override val yPos: Float,
    private val width: Float,
    private val height: Float,
    private val color: Long
) : GuiElement {
    override fun render(gui: Gui) {
        gui.drawRect(this.xPos, this.yPos, this.width, this.height, this.color)
    }
}