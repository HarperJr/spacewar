package com.harper.spacewar.main.gui.impl.element

import com.harper.spacewar.Color
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.texture.Texture
import com.harper.spacewar.main.gui.Gui
import com.harper.spacewar.main.gui.GuiElement
import java.text.DecimalFormat
import kotlin.math.max
import kotlin.math.min

class GuiHealthBar(
    override val xPos: Float,
    override val yPos: Float,
    private val valueProvider: () -> Float
) : GuiElement {
    private val valueFormatter: DecimalFormat = DecimalFormat("# %")

    override fun render(gui: Gui) {
        GlUtils.glPopMatrix()
        GlUtils.glBlendSeparateDefault()

        val value = valueProvider.invoke()
        val texture = gui.textureManager.provideTexture("gui/game.png")

        renderBackgroundBar(gui, texture)
        renderForegroundBar(gui, texture, value)

        val tileHeight = texture.height / 8f
        gui.drawCenteredText(
            valueFormatter.format(value),
            this.xPos,
            this.yPos + tileHeight / 2f,
            0xffffffff,
            1f
        )

        GlUtils.glPushMatrix()
    }

    private fun renderBackgroundBar(gui: Gui, texture: Texture) {
        val tileHeight = texture.height / 8f
        gui.drawTexturedRect(
            texture,
            this.xPos - texture.width / 2f,
            this.yPos,
            texture.width / 2f,
            tileHeight,
            0f,
            tileHeight * 2f
        )

        gui.drawTexturedRect(
            texture,
            this.xPos,
            this.yPos,
            texture.width / 2f,
            tileHeight,
            0f,
            tileHeight * 3f
        )
    }

    private fun renderForegroundBar(gui: Gui, texture: Texture, value: Float) {
        val tileHeight = texture.height / 8f
        gui.drawTexturedRect(
            texture,
            this.xPos - texture.width / 2f,
            this.yPos,
            min(1f, value * 2f) * texture.width / 2f,
            tileHeight,
            0f,
            0f,
            Color.GREEN
        )

        gui.drawTexturedRect(
            texture,
            this.xPos,
            this.yPos,
            max(0f, (value - 0.5f) * 2f) * texture.width / 2f,
            tileHeight,
            0f,
            tileHeight,
            Color.GREEN
        )
    }
}