package com.harper.spacewar.main.gui.impl

import com.harper.spacewar.main.gl.font.FontRenderer
import com.harper.spacewar.main.gl.texture.TextureManager
import com.harper.spacewar.main.gui.GuiContainer
import com.harper.spacewar.main.gui.impl.element.GuiButton
import com.harper.spacewar.main.gui.impl.element.GuiLabel
import com.harper.spacewar.main.gui.listener.OnClickListener
import com.harper.spacewar.main.scene.SceneMainMenu

class GuiMainMenu(private val scene: SceneMainMenu, fontRenderer: FontRenderer, textureManager: TextureManager) :
    GuiContainer(fontRenderer, textureManager) {

    override fun inflateGui(scaledWidth: Float, scaledHeight: Float) {
        val offsetRight = scaledWidth - 160f
        addGuiElement(
            GuiButton(
                ENTER_BTN_ID,
                offsetRight,
                scaledHeight / 2f - 32f,
                140f,
                16f,
                "Enter the game"
            ).also { it.onClickListener = onClickListener }
        )

        addGuiElement(
            GuiButton(
                LEAVE_BTN_ID,
                offsetRight,
                scaledHeight / 2f,
                140f,
                16f,
                "Leave the game"
            ).also { it.onClickListener = onClickListener }
        )

        addGuiElement(GuiLabel(4f, scaledHeight - 12f, "Spacewar v 0.2 alpha", false))
        addGuiElement(GuiLabel(scaledWidth / 2f, 16f, "SPACEWAR THE GAME", true, 0xffffffff, 2f))
    }

    private val onClickListener = object : OnClickListener {
        override fun onClicked(button: GuiButton, x: Float, y: Float) {
            when (button.id) {
                ENTER_BTN_ID -> scene.onEnterBtnClicked()
                LEAVE_BTN_ID -> scene.onLeaveBtnClicked()
            }
        }
    }

    companion object {
        private const val ENTER_BTN_ID = "btn.enter"
        private const val LOAD_BTN_ID = "btn.asyncLoad"
        private const val LEAVE_BTN_ID = "btn.leave"
    }
}