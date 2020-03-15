package com.harper.spacewar.main.gui.impl

import com.harper.spacewar.main.Spacewar
import com.harper.spacewar.main.gui.GuiContainer

class GuiMainMenu(spacewar: Spacewar) : GuiContainer(spacewar) {
    override fun inflateGui(scaledWidth: Float, scaledHeight: Float) {
        val offsetRight = scaledWidth - scaledWidth / 4f - 60f
        addGuiElement(GuiButton("Enter the game", offsetRight, scaledHeight / 2f - 32f, 140f, 16f))
        addGuiElement(GuiButton("Load the game", offsetRight, scaledHeight / 2f, 140f, 16f))
        addGuiElement(GuiButton("Leave the game", offsetRight, scaledHeight / 2f + 32f, 140f, 16f))
    }
}