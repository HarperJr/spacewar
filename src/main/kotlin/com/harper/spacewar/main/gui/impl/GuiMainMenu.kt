package com.harper.spacewar.main.gui.impl

import com.harper.spacewar.main.Spacewar
import com.harper.spacewar.main.gui.GuiContainer

class GuiMainMenu(spacewar: Spacewar) : GuiContainer(spacewar) {
    override fun inflateGui(scaledWidth: Float, scaledHeight: Float) {
        addGuiElement(
            GuiButton(
                "Enter the game",
                scaledWidth / 2f - 80f,
                scaledHeight / 2f - 100f,
                160f,
                20f
            )
        )

        addGuiElement(
            GuiButton(
                "Enter the game",
                scaledWidth / 2f - 80f,
                scaledHeight / 2f - 140f,
                160f,
                20f
            )
        )

        addGuiElement(
            GuiButton(
                "Enter the game",
                scaledWidth / 2f - 80f,
                scaledHeight / 2f - 180f,
                160f,
                20f
            )
        )
    }
}