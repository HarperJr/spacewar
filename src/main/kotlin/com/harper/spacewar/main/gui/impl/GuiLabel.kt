package com.harper.spacewar.main.gui.impl

import com.harper.spacewar.main.gl.font.FontDrawer
import com.harper.spacewar.main.gui.Gui
import com.harper.spacewar.main.gui.listener.OnAnimateLabelListener

class GuiLabel(
    private val centered: Boolean = false,
    var text: String,
    var x: Float,
    var y: Float,
    var color: Long = 0xffffffff,
    var scale: Float = 1f
) : Gui() {
    var onAnimateLabelListener: OnAnimateLabelListener? = null

    fun drawLabel(fontDrawer: FontDrawer) {
        onAnimateLabelListener?.onAnimate(this)
        if (centered) {
            drawCenteredText(fontDrawer, text, x, y, color, scale)
        } else {
            drawText(fontDrawer, text, x, y, color, scale)
        }
    }
}
