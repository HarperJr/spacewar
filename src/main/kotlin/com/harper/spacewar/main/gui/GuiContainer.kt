package com.harper.spacewar.main.gui

import com.harper.spacewar.main.gl.font.FontDrawer
import com.harper.spacewar.main.gl.texture.TextureManager
import com.harper.spacewar.main.gui.impl.GuiButton
import com.harper.spacewar.main.gui.impl.GuiLabel
import com.harper.spacewar.main.resolution.ScaledResolution

abstract class GuiContainer(private val fontDrawer: FontDrawer, private val textureManager: TextureManager) : Gui() {
    private val buttons: MutableList<GuiButton> = mutableListOf()
    private val labels: MutableList<GuiLabel> = mutableListOf()

    abstract fun inflateGui(scaledWidth: Float, scaledHeight: Float)

    fun drawGui() {
        for (btn in buttons)
            btn.drawButton(fontDrawer, textureManager)
        for (label in labels)
            label.drawLabel(fontDrawer)
    }

    fun onResolutionChanged(scaledResolution: ScaledResolution) {
        if (buttons.isNotEmpty())
            buttons.clear()
        if (labels.isNotEmpty())
            labels.clear()
        inflateGui(scaledResolution.scaledWidth, scaledResolution.scaledHeight)
    }

    fun onClicked(x: Float, y: Float) {
        for (btn in buttons)
            btn.onClicked(x, y)
    }

    fun onMoved(x: Float, y: Float) {
        for (btn in buttons)
            btn.onMoved(x, y)
    }

    protected fun addGuiElement(guiElement: Gui) {
        when (guiElement) {
            is GuiButton -> buttons.add(guiElement)
            is GuiLabel -> labels.add(guiElement)
        }
    }
}
