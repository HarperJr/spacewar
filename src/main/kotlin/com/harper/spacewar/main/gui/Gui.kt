package com.harper.spacewar.main.gui

import com.harper.spacewar.Color
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.buffer.VertexFormat
import com.harper.spacewar.main.gl.font.FontRenderer
import com.harper.spacewar.main.gl.tessellator.Tessellator
import com.harper.spacewar.main.gl.texture.Texture
import com.harper.spacewar.main.gl.texture.TextureManager

open class Gui(private val fontRenderer: FontRenderer, val textureManager: TextureManager) {
    private val tessellator = Tessellator.instance

    fun drawRect(left: Float, bottom: Float, right: Float, top: Float, color: Long) {
        GlUtils.glPopMatrix()
        GlUtils.glDisable(GlUtils.TEXTURE_2D)
        GlUtils.glEnable(GlUtils.BLEND)
        GlUtils.glBlendFuncDefault()

        tessellator.tessellate(GlUtils.DRAW_MODE_QUADS, VertexFormat.POSITION_COLOR) {
            pos(left, top, 0f).color(color).completeVertex()
            pos(right, top, 0f).color(color).completeVertex()
            pos(right, bottom, 0f).color(color).completeVertex()
            pos(left, bottom, 0f).color(color).completeVertex()
        }

        GlUtils.glDisable(GlUtils.BLEND)
        GlUtils.glEnable(GlUtils.TEXTURE_2D)
        GlUtils.glPushMatrix()
    }

    fun drawTexturedRect(texture: Texture, x: Float, y: Float, width: Float, height: Float, texX: Float, texY: Float, color: Long = Color.WHITE) {
        GlUtils.glPopMatrix()
        GlUtils.glEnable(GlUtils.TEXTURE_2D)
        GlUtils.glBindTexture(texture.glTexture)
        GlUtils.glEnable(GlUtils.BLEND)
        GlUtils.glColor(0xffffffff)

        tessellator.tessellate(GlUtils.DRAW_MODE_QUADS, VertexFormat.POSITION_TEX_COLOR) {
            pos(x, y + height, 0f).tex(texX / texture.width, (texY + height) / texture.height)
                .color(color).completeVertex()

            pos(x + width, y + height, 0f).tex((texX + width) / texture.width, (texY + height) / texture.height)
                .color(color).completeVertex()

            pos(x + width, y, 0f).tex((texX + width) / texture.width, texY / texture.height)
                .color(color).completeVertex()

            pos(x, y, 0f).tex(texX / texture.width, texY / texture.height)
                .color(color).completeVertex()
        }

        GlUtils.glDisable(GlUtils.BLEND)
        GlUtils.glDisable(GlUtils.TEXTURE_2D)
        GlUtils.glPushMatrix()
    }

    fun drawText(text: String, x: Float, y: Float, color: Long, scaleFactor: Float) {
        fontRenderer.drawText(text, x, y, color, scaleFactor)
    }

    fun drawCenteredText(text: String, x: Float, y: Float, color: Long, scaleFactor: Float) {
        fontRenderer.drawCenteredText(text, x, y, color, scaleFactor)
    }
}