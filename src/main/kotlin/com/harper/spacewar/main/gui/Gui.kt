package com.harper.spacewar.main.gui

import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.buffer.VertexFormat
import com.harper.spacewar.main.gl.font.FontDrawer
import com.harper.spacewar.main.gl.tessellator.Tessellator

open class Gui {
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

    protected fun drawTexturedRect(x: Float, y: Float, width: Float, height: Float, texX: Float, texY: Float, texWidth: Float, texHeight: Float) {
        GlUtils.glPopMatrix()
        GlUtils.glEnable(GlUtils.TEXTURE_2D)
        GlUtils.glDisable(GlUtils.BLEND)
        GlUtils.glColor(0xffffffff)

        tessellator.tessellate(GlUtils.DRAW_MODE_QUADS, VertexFormat.POSITION_TEX) {
            pos(x, y + height, 0f).tex(texX / texWidth, (texY + height) / texHeight).completeVertex()
            pos(x + width, y + height, 0f).tex((texX + width) / texWidth, (texY + height) / texHeight).completeVertex()
            pos(x + width, y, 0f).tex((texX + width) / texHeight, texY / texHeight).completeVertex()
            pos(x, y, 0f).tex(texX / texWidth, texY / texHeight).completeVertex()
        }

        GlUtils.glDisable(GlUtils.BLEND)
        GlUtils.glDisable(GlUtils.TEXTURE_2D)
        GlUtils.glPushMatrix()
    }

    protected fun drawText(fontDrawer: FontDrawer, text: String, x: Float, y: Float, color: Long, scaleFactor: Float) {
        fontDrawer.drawText(text, x, y, color, scaleFactor)
    }

    protected fun drawCenteredText(fontDrawer: FontDrawer, text: String, x: Float, y: Float, color: Long, scaleFactor: Float) {
        fontDrawer.drawCenteredText(text, x, y, color, scaleFactor)
    }
}