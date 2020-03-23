package com.harper.spacewar.main.gui.impl

import com.harper.spacewar.main.gl.font.FontRenderer
import com.harper.spacewar.main.gl.texture.TextureManager
import com.harper.spacewar.main.gui.GuiContainer
import com.harper.spacewar.main.gui.impl.element.GuiButton
import com.harper.spacewar.main.gui.impl.element.GuiLabel
import com.harper.spacewar.main.gui.impl.element.GuiPanel
import com.harper.spacewar.main.gui.listener.OnClickListener
import com.harper.spacewar.main.scene.SceneInGame

class GuiInGameMenu(private val scene: SceneInGame, fontRenderer: FontRenderer, textureManager: TextureManager) : GuiContainer(fontRenderer, textureManager) {
    override fun inflateGui(scaledWidth: Float, scaledHeight: Float) {
        addGuiElement(
            GuiPanel(0f, 0f, scaledWidth, scaledHeight, 0x00000044)
        )

        addGuiElement(
            GuiLabel(scaledWidth / 2f, scaledHeight /2f - 64f, "Paused", true, 0xffaaaaff, 2f)
        )

        addGuiElement(
            GuiButton(CONTINUE_BTN_ID, scaledWidth / 2f, scaledHeight / 2f - 32f, 120f, 16f, "Continue", centered = true)
                .also { it.onClickListener = this.onClickListener }
        )

        addGuiElement(
            GuiButton(SAVE_BTN_ID, scaledWidth / 2f, scaledHeight / 2f, 120f, 16f, "Save", centered = true)
                .also { it.onClickListener = this.onClickListener }
        )

        addGuiElement(
            GuiButton(LEAVE_BTN_ID, scaledWidth / 2f, scaledHeight / 2f + 32f, 120f, 16f, "To main menu", centered = true)
                .also { it.onClickListener = this.onClickListener }
        )
    }

    private val onClickListener = object : OnClickListener {
        override fun onClicked(button: GuiButton, x: Float, y: Float) {
            when (button.id) {
                CONTINUE_BTN_ID -> scene.onContinueBtnClicked()
                SAVE_BTN_ID -> scene.onSaveBtnClicked()
                LEAVE_BTN_ID -> scene.onLeaveBtnClicked()
            }
        }
    }

    companion object {
        private const val CONTINUE_BTN_ID = "btn.continue"
        private const val SAVE_BTN_ID = "btn.save"
        private const val LEAVE_BTN_ID = "btn.leave"
    }
}
