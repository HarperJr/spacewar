package com.harper.spacewar.main.gui.impl.element

import com.harper.spacewar.display.listener.MouseListener
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gui.Gui
import com.harper.spacewar.main.gui.GuiElement
import com.harper.spacewar.main.gui.listener.OnClickListener

class GuiButton(
    val id: String,
    override val xPos: Float,
    override val yPos: Float,
    private val width: Float,
    private val height: Float,
    private var text: String,
    private var centered: Boolean = false
) : GuiElement, MouseListener {
    var onClickListener: OnClickListener? = null
    private var isHovered: Boolean = false

    override fun render(gui: Gui) {
        GlUtils.glPopMatrix()
        GlUtils.glEnable(GlUtils.BLEND)
        GlUtils.glEnable(GlUtils.TEXTURE_2D)
        GlUtils.glBlendFuncDefault()
        GlUtils.glBlendSeparateDefault()

        GlUtils.glColor(0xffffffff)

        val texture = gui.textureManager.provideTexture("gui/button.png")
        val tileHeight = texture.height / 4f
        gui.drawTexturedRect(
            texture,
            if (this.centered) this.xPos - this.width / 2f else this.xPos,
            this.yPos,
            this.width / 2f,
            this.height,
            0f,
            getHoveredState(this.isHovered) * tileHeight
        )

        gui.drawTexturedRect(
            texture,
            if (this.centered) this.xPos else this.xPos + this.width / 2f,
            this.yPos,
            this.width / 2f,
            this.height,
            texture.width - this.width / 2f,
            getHoveredState(this.isHovered) * tileHeight + tileHeight
        )

        val textColor = if (this.isHovered) 0xefefaaff else 0xffffffff
        gui.drawCenteredText(
            this.text,
            if (centered) this.xPos else this.xPos + this.width / 2f,
            this.yPos + this.height / 2f,
            textColor,
            1f
        )

        GlUtils.glDisable(GlUtils.BLEND)
        GlUtils.glDisable(GlUtils.TEXTURE_2D)
        GlUtils.glPushMatrix()
    }

    private fun getHoveredState(isHovered: Boolean): Int {
        return if (isHovered) 2 else 0
    }

    override fun onPressed(x: Float, y: Float) {
        /** Not implemented **/
    }

    override fun onClicked(x: Float, y: Float) {
        if (this.isHovered)
            onClickListener?.onClicked(this, x, y)
    }

    override fun onScrolled(x: Float, y: Float) {
        /** Not implemented **/
    }

    override fun onMoved(x: Float, y: Float) {
        val adjustedXPos = if (this.centered) this.xPos - this.width / 2f else this.xPos
        this.isHovered = x >= adjustedXPos && x <= adjustedXPos + this.width && y >= this.yPos && y <= this.yPos + this.height
    }
}