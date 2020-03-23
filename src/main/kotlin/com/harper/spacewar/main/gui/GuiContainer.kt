package com.harper.spacewar.main.gui

import com.harper.spacewar.display.listener.MouseListener
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.font.FontRenderer
import com.harper.spacewar.main.gl.texture.TextureManager
import com.harper.spacewar.main.resolution.ScaledResolution

abstract class GuiContainer(fontRenderer: FontRenderer, textureManager: TextureManager) : Gui(fontRenderer, textureManager) {
    private val guiMouseListeners: MutableList<MouseListener> = mutableListOf()
    private val guiElements: MutableList<GuiElement> = mutableListOf()

    abstract fun inflateGui(scaledWidth: Float, scaledHeight: Float)

    fun render(time: Float) {
        GlUtils.glEnable(GlUtils.DEPTH_TEST)
        GlUtils.glDepthFunc(GlUtils.DEPTH_ALWAYS)
        GlUtils.glEnableDepthMask()

        for (guiElement in this.guiElements)
            guiElement.render(this)

        GlUtils.glDisable(GlUtils.DEPTH_TEST)
    }

    fun onResolutionChanged(scaledResolution: ScaledResolution) {
        this.guiElements.clear()
        this.guiMouseListeners.clear()
        inflateGui(scaledResolution.scaledWidth, scaledResolution.scaledHeight)
    }

    fun onClicked(x: Float, y: Float) {
        for (btn in guiMouseListeners)
            btn.onClicked(x, y)
    }

    fun onMoved(x: Float, y: Float) {
        for (btn in guiMouseListeners)
            btn.onMoved(x, y)
    }

    protected fun addGuiElement(guiElement: GuiElement) {
        when (guiElement) {
            is MouseListener -> guiMouseListeners.add(guiElement)
            else -> {
                /** Not implemented, just skip **/
            }
        }
        this.guiElements.add(guiElement)
    }
}
