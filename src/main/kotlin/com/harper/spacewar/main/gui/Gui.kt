package com.harper.spacewar.main.gui

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
        GlUtils.glColor(color)

        tessellator.tessellate(GlUtils.DRAW_MODE_QUADS, VertexFormat.POSITION) {
            pos(left, top, 0f).completeVertex()
            pos(right, top, 0f).completeVertex()
            pos(right, bottom, 0f).completeVertex()
            pos(left, bottom, 0f).completeVertex()
        }

        GlUtils.glDisable(GlUtils.BLEND)
        GlUtils.glEnable(GlUtils.TEXTURE_2D)
        GlUtils.glPushMatrix()
    }

    fun drawTexturedRect(texture: Texture, x: Float, y: Float, width: Float, height: Float, texX: Float, texY: Float) {
        GlUtils.glPopMatrix()
        GlUtils.glEnable(GlUtils.TEXTURE_2D)
        GlUtils.glBindTexture(texture.glTexture)
        GlUtils.glEnable(GlUtils.BLEND)
        GlUtils.glColor(0xffffffff)

        tessellator.tessellate(GlUtils.DRAW_MODE_QUADS, VertexFormat.POSITION_TEX) {
            pos(x, y + height, 0f).tex(texX / texture.width, (texY + height) / texture.height).completeVertex()
            pos(x + width, y + height, 0f).tex((texX + width) / texture.width, (texY + height) / texture.height).completeVertex()
            pos(x + width, y, 0f).tex((texX + width) / texture.width, texY / texture.height).completeVertex()
            pos(x, y, 0f).tex(texX / texture.width, texY / texture.height).completeVertex()
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