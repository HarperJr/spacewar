package com.harper.spacewar.main.gui

import com.harper.spacewar.main.Spacewar
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.buffer.VertexFormat
import com.harper.spacewar.main.gl.font.FontDrawer
import com.harper.spacewar.main.gl.tessellator.Tessellator

abstract class Gui(private val spacewar: Spacewar) {
    private val tessellator = Tessellator.instance

    abstract fun draw()

    protected fun drawRect(fromX: Float, fromY: Float, toX: Float, toY: Float, color: Int) {
        tessellator.tessellate(GlUtils.DRAW_MODE_TRIANGLES, VertexFormat.POSITION) {

        }
    }

    protected fun drawText(text: String, x: Float, y: Float, color: Long, scaleFactor: Float) {
        spacewar.fontDrawer.drawText(text, x, y, color, scaleFactor)
    }
}