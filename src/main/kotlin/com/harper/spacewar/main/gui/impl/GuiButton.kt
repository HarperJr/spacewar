package com.harper.spacewar.main.gui.impl

import com.harper.spacewar.display.listener.MouseListener
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.font.FontDrawer
import com.harper.spacewar.main.gl.texture.TextureManager
import com.harper.spacewar.main.gui.Gui
import com.harper.spacewar.main.gui.listener.OnClickListener

class GuiButton(var text: String, val xPos: Float, val yPos: Float, val width: Float, val height: Float) : Gui(),
    MouseListener {
    var onClickListener: OnClickListener? = null
    var isHovered: Boolean = false
        private set

    fun drawButton(fontDrawer: FontDrawer, textureManager: TextureManager) {
        GlUtils.glPopMatrix()
        GlUtils.glEnable(GlUtils.BLEND)
        GlUtils.glBlendFuncDefault()

        textureManager.bind("gui/button.png")
        drawTexturedRect(this.xPos, this.yPos, this.width, this.height, 0f, getHoveredState(this.isHovered) * 16f, 64f, 64f)
        drawCenteredText(fontDrawer, this.text, this.xPos + this.width / 2f, this.yPos + this.height / 2f, 0xffffffff, 1f)

        GlUtils.glDisable(GlUtils.BLEND)
        GlUtils.glPushMatrix()
    }

    private fun getHoveredState(isHovered: Boolean): Int {
        return if (isHovered) 2 else 0
    }

    override fun onClicked(x: Float, y: Float) {
        if (isHovered)
            onClickListener?.onClicked(this, x, y)
    }

    override fun onPressed(x: Float, y: Float) {
        // No implementation here
    }

    override fun onMoved(x: Float, y: Float) {
        isHovered = x >= this.xPos && x <= this.xPos + this.width && y >= this.yPos && y <= this.yPos + this.height
    }
}