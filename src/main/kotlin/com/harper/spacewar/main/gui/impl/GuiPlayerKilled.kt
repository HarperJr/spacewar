package com.harper.spacewar.main.gui.impl

import com.harper.spacewar.main.gl.font.FontRenderer
import com.harper.spacewar.main.gl.texture.TextureManager
import com.harper.spacewar.main.gui.GuiContainer
import com.harper.spacewar.main.gui.impl.element.GuiButton
import com.harper.spacewar.main.gui.impl.element.GuiLabel
import com.harper.spacewar.main.gui.impl.element.GuiPanel
import com.harper.spacewar.main.gui.listener.OnClickListener
import com.harper.spacewar.main.scene.SceneInGame

class GuiPlayerKilled(private val scene: SceneInGame, fontRenderer: FontRenderer, textureManager: TextureManager) :
    GuiContainer(fontRenderer, textureManager) {
    override fun inflateGui(scaledWidth: Float, scaledHeight: Float) {
        addGuiElement(
            GuiPanel(0f, 0f, scaledWidth, scaledHeight, 0x00000044)
        )

        addGuiElement(
            GuiLabel(scaledWidth / 2f, scaledHeight / 2f - 64f, "You died", true, 0xffaaaaff, 2f)
        )

        addGuiElement(
            GuiButton(RESTART_BTN_ID, scaledWidth / 2f, scaledHeight / 2f - 32f, 120f, 16f, "Restart", true)
                .also { it.onClickListener = onClickListener }
        )

        addGuiElement(
            GuiButton(LEAVE_BTN_ID, scaledWidth / 2f, scaledHeight / 2f, 120f, 16f, "To main menu", true)
                .also { it.onClickListener = onClickListener }
        )
    }

    private val onClickListener = object : OnClickListener {
        override fun onClicked(button: GuiButton, x: Float, y: Float) {
            when (button.id) {
                GuiPlayerKilled.RESTART_BTN_ID -> scene.onRestartBtnClicked()
                GuiPlayerKilled.LEAVE_BTN_ID -> scene.onLeaveBtnClicked()
            }
        }
    }

    companion object {
        private const val RESTART_BTN_ID = "btn.restart"
        private const val LEAVE_BTN_ID = "btn.leave"
    }
}
