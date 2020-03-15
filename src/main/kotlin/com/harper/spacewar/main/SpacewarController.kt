package com.harper.spacewar.main

import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.font.FontDrawer

class SpacewarController(private val spacewar: Spacewar) {
    private val fontDrawer: FontDrawer
        get() = spacewar.fontDrawer

    fun update() {
        renderOverlay()
    }

    fun destroy() {

    }

    private fun renderOverlay() {
        setupCamera()

        fontDrawer.drawText("452 fps", 0f, 0f, 0xffffffff, 2f)
    }

    private fun setupCamera() {
        val (scaledWidth, scaledHeight) = spacewar.scaledResolution
        GlUtils.glMatrixMode(GlUtils.PROJECTION)
        GlUtils.glLoadIdentity()
        GlUtils.glOrtho(0.0, scaledWidth.toDouble(), scaledHeight.toDouble(), 0.0, 0.1, 1000.0)
        GlUtils.glMatrixMode(GlUtils.MODELVIEW)
        GlUtils.glLoadIdentity()
        GlUtils.glTranslatef(0f, 0f, -100f)
    }
}
