package com.harper.spacewar.main.gui.impl.element

import com.harper.spacewar.main.gui.Gui
import com.harper.spacewar.main.gui.GuiElement

class GuiCursor(override val xPos: Float, override val yPos: Float) : GuiElement {
    override fun render(gui: Gui) {
        val texture = gui.textureManager.provideTexture("gui/game.png")
        val spriteWidth = texture.width / 8f
        val spriteHeight = texture.height / 8f
        gui.drawTexturedRect(
            texture,
            this.xPos - spriteWidth / 2f,
            this.yPos - spriteHeight / 2f,
            spriteWidth,
            spriteHeight,
            spriteWidth * 7f,
            0f
        )
    }
}
