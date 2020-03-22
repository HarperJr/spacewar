package com.harper.spacewar.main.gui.impl.element

import com.harper.spacewar.main.gui.Gui
import com.harper.spacewar.main.gui.GuiElement
import com.harper.spacewar.main.gui.listener.OnAnimateLabelListener

class GuiLabel(
    override var xPos: Float,
    override var yPos: Float,
    private var text: String,
    private val centered: Boolean = false,
    private var color: Long = 0xffffffff,
    private var scale: Float = 1f
) : GuiElement {
    var onAnimateLabelListener: OnAnimateLabelListener? = null

    override fun render(gui: Gui) {
        onAnimateLabelListener?.onAnimate(this)

        if (centered) {
            gui.drawCenteredText(text, xPos, yPos, color, scale)
        } else {
            gui.drawText(text, xPos, yPos, color, scale)
        }
    }
}
