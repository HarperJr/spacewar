package com.harper.spacewar.main.gui.impl

import com.harper.spacewar.main.gl.font.FontRenderer
import com.harper.spacewar.main.gl.texture.TextureManager
import com.harper.spacewar.main.gui.GuiContainer
import com.harper.spacewar.main.gui.impl.element.GuiCursor
import com.harper.spacewar.main.gui.impl.element.GuiHealthBar
import com.harper.spacewar.main.gui.impl.element.GuiLabel
import com.harper.spacewar.main.gui.impl.element.GuiPanel
import com.harper.spacewar.main.gui.listener.OnAnimateLabelListener
import com.harper.spacewar.main.scene.SceneInGame

class GuiInGame(private val scene: SceneInGame, fontRenderer: FontRenderer, textureManager: TextureManager) :
    GuiContainer(fontRenderer, textureManager) {
    private var isDebugging = false

    override fun inflateGui(scaledWidth: Float, scaledHeight: Float) {
        addGuiElement(
            GuiHealthBar(scaledWidth / 2f, scaledHeight - 20f) {
                return@GuiHealthBar 0.35f
            }
        )

        addGuiElement(
            GuiCursor(scaledWidth / 2f, scaledHeight / 2f)
        )

        if (this.isDebugging) {
            addGuiElement(
                GuiPanel(0f, 0f, scaledWidth / 4f, scaledHeight / 4f, 0x00000044)
            )

            addDebugPlayerPositionElements()
        }
    }

    private fun addDebugPlayerPositionElements() {
        addGuiElement(
            GuiLabel(4f, 4f, "undefined").apply {
                onAnimateLabelListener = object : OnAnimateLabelListener {
                    override fun onAnimate(label: GuiLabel) {
                        label.text = "Fps: ${scene.fps}"
                    }
                }
            }
        )

        addGuiElement(
            GuiLabel(4f, 12f, "undefined").apply {
                onAnimateLabelListener = object : OnAnimateLabelListener {
                    override fun onAnimate(label: GuiLabel) {
                        label.text = "posX: ${scene.playerEntity.position.x}"
                    }
                }
            }
        )

        addGuiElement(
            GuiLabel(4f, 20f, "undefined").apply {
                onAnimateLabelListener = object : OnAnimateLabelListener {
                    override fun onAnimate(label: GuiLabel) {
                        label.text = "posY: ${scene.playerEntity.position.y}"
                    }
                }
            }
        )

        addGuiElement(
            GuiLabel(4f, 28f, "undefined").apply {
                onAnimateLabelListener = object : OnAnimateLabelListener {
                    override fun onAnimate(label: GuiLabel) {
                        label.text = "posZ: ${scene.playerEntity.position.z}"
                    }
                }
            }
        )
    }

    fun switchDebuggingState() {
        this.isDebugging = !this.isDebugging
    }
}