package com.harper.spacewar.main.gui.impl

import com.harper.spacewar.logging.Logger
import com.harper.spacewar.main.gl.font.FontDrawer
import com.harper.spacewar.main.gl.texture.TextureManager
import com.harper.spacewar.main.gui.GuiContainer
import com.harper.spacewar.main.gui.listener.OnClickListener
import com.harper.spacewar.main.scene.SceneMainMenu

class GuiMainMenu(private val scene: SceneMainMenu, fontDrawer: FontDrawer, textureManager: TextureManager) :
    GuiContainer(fontDrawer, textureManager) {
    private val logger = Logger.getLogger<GuiMainMenu>()

    override fun inflateGui(scaledWidth: Float, scaledHeight: Float) {
        val offsetRight = scaledWidth - 160f
        addGuiElement(
            GuiButton(ENTER_BTN_ID, "Enter the game", offsetRight, scaledHeight / 2f - 32f, 140f, 16f)
                .also { it.onClickListener = onClickListener }
        )
        addGuiElement(
            GuiButton(LOAD_BTN_ID, "Load the game", offsetRight, scaledHeight / 2f, 140f, 16f)
                .also { it.onClickListener = onClickListener }
        )
        addGuiElement(
            GuiButton(LEAVE_BTN_ID, "Leave the game", offsetRight, scaledHeight / 2f + 32f, 140f, 16f)
                .also { it.onClickListener = onClickListener }
        )
        addGuiElement(
            GuiLabel(false, "Spacewar v 0.1 dev", 4f, scaledHeight - 12f)
        )
    }

    private val onClickListener = object : OnClickListener {
        override fun onClicked(button: GuiButton, x: Float, y: Float) {
            when (button.id) {
                ENTER_BTN_ID -> scene.onEnterBtnClicked()
                LOAD_BTN_ID -> scene.onLoadBtnClicked()
                LEAVE_BTN_ID -> scene.onLeaveBtnClicked()
            }
        }
    }

    companion object {
        private const val ENTER_BTN_ID = "btn.enter"
        private const val LOAD_BTN_ID = "btn.load"
        private const val LEAVE_BTN_ID = "btn.leave"
    }
}