package com.harper.spacewar.main.gui.impl.element

import com.harper.spacewar.main.gui.Gui
import com.harper.spacewar.main.gui.GuiElement
import com.harper.spacewar.main.gui.listener.OnAnimateCursorListener

class GuiCursor(override val xPos: Float, override val yPos: Float) : GuiElement {
    var onAnimateCursorListener: OnAnimateCursorListener? = null

    var aimCursorX: Float = 0f
    var aimCursorY: Float = 0f

    override fun render(gui: Gui) {
        onAnimateCursorListener?.onAnimate(this)

        val texture = gui.textureManager.provideTexture("gui/game.png")
        val spriteWidth = texture.width / 8f
        val spriteHeight = texture.height / 8f

        gui.drawTexturedRect(
            texture,
            this.aimCursorX - spriteWidth / 2f,
            this.aimCursorY - spriteHeight / 2f,
            spriteWidth,
            spriteHeight,
            spriteWidth * 7f,
            spriteHeight
        )

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
