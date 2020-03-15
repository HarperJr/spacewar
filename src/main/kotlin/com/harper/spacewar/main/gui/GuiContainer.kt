package com.harper.spacewar.main.gui

import com.harper.spacewar.display.listener.MouseListener
import com.harper.spacewar.main.Spacewar
import com.harper.spacewar.main.gl.font.FontDrawer
import com.harper.spacewar.main.gl.texture.TextureManager
import com.harper.spacewar.main.gui.impl.GuiButton
import com.harper.spacewar.main.resolution.ScaledResolution

abstract class GuiContainer(private val spacewar: Spacewar) : Gui(), MouseListener {
    private val fontDrawer: FontDrawer
        get() = spacewar.fontDrawer
    private val textureManager: TextureManager
        get() = spacewar.textureManager

    private val buttons: MutableList<GuiButton> = mutableListOf()

    abstract fun inflateGui(scaledWidth: Float, scaledHeight: Float)

    fun drawGui() {
        for (btn in buttons)
            btn.drawButton(fontDrawer, textureManager)
    }

    fun onResolutionChanged(scaledResolution: ScaledResolution) {
        if (buttons.isNotEmpty())
            buttons.clear()
        inflateGui(scaledResolution.scaledWidth, scaledResolution.scaledHeight)
    }

    override fun onClicked(x: Float, y: Float) {
        for (btn in buttons)
            btn.onClicked(x, y)
    }

    override fun onPressed(x: Float, y: Float) {
        for (btn in buttons)
            btn.onPressed(x, y)
    }

    override fun onMoved(x: Float, y: Float) {
        for (btn in buttons)
            btn.onMoved(x, y)
    }

    protected fun addGuiElement(guiElement: Gui) {
        if (guiElement is GuiButton)
            buttons.add(guiElement)
    }
}
