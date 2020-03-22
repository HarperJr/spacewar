package com.harper.spacewar.main.gui.impl

import com.harper.spacewar.main.gl.font.FontDrawer
import com.harper.spacewar.main.gl.texture.TextureManager
import com.harper.spacewar.main.gui.GuiContainer
import com.harper.spacewar.main.gui.impl.element.GuiHealthBar

class GuiInGame(fontDrawer: FontDrawer, textureManager: TextureManager) : GuiContainer(fontDrawer, textureManager) {
    override fun inflateGui(scaledWidth: Float, scaledHeight: Float) {
        addGuiElement(
            GuiHealthBar(scaledWidth / 2f, scaledHeight - 20f) {
                return@GuiHealthBar 0.15f
            }
        )
    }
}