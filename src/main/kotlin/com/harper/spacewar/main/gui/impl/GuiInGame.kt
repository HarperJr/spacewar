package com.harper.spacewar.main.gui.impl

import com.harper.spacewar.main.Camera
import com.harper.spacewar.main.gl.font.FontRenderer
import com.harper.spacewar.main.gl.texture.TextureManager
import com.harper.spacewar.main.gui.GuiContainer
import com.harper.spacewar.main.gui.impl.element.GuiCursor
import com.harper.spacewar.main.gui.impl.element.GuiHealthBar
import com.harper.spacewar.main.gui.impl.element.GuiLabel
import com.harper.spacewar.main.gui.impl.element.GuiPanel
import com.harper.spacewar.main.gui.listener.OnAnimateCursorListener
import com.harper.spacewar.main.gui.listener.OnAnimateLabelListener
import com.harper.spacewar.main.scene.SceneInGame
import org.joml.Vector2f

class GuiInGame(private val scene: SceneInGame, fontRenderer: FontRenderer, textureManager: TextureManager) :
    GuiContainer(fontRenderer, textureManager) {
    private val playerAimVec: Vector2f
        get() {
            return Vector2f(
                scene.entityPlayer.lookAt.x,
                scene.entityPlayer.lookAt.y
            )
        }

    private val camera: Camera
        get() = scene.camera

    private var isDebugging = false

    override fun inflateGui(scaledWidth: Float, scaledHeight: Float) {
        addGuiElement(
            GuiHealthBar(scaledWidth / 2f, scaledHeight - 20f) {
                return@GuiHealthBar 0.35f
            }
        )

        addGuiElement(
            GuiCursor(scaledWidth / 2f, scaledHeight / 2f).also {
                it.onAnimateCursorListener = object : OnAnimateCursorListener {
                    override fun onAnimate(cursor: GuiCursor) {
                        cursor.aimCursorX = (playerAimVec.x - camera.lookVec.x) * scaledWidth + scaledWidth / 2f
                        cursor.aimCursorY = (playerAimVec.y - camera.lookVec.y) * scaledHeight + scaledHeight / 2f
                    }
                }
            }
        )

        if (this.isDebugging) {
            addGuiElement(
                GuiPanel(0f, 0f, scaledWidth / 4f, 82f, 0x00000044)
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
            GuiLabel(4f, 16f, "undefined").apply {
                onAnimateLabelListener = object : OnAnimateLabelListener {
                    override fun onAnimate(label: GuiLabel) {
                        label.text = "posX: ${scene.entityPlayer.position.x}"
                    }
                }
            }
        )

        addGuiElement(
            GuiLabel(4f, 28f, "undefined").apply {
                onAnimateLabelListener = object : OnAnimateLabelListener {
                    override fun onAnimate(label: GuiLabel) {
                        label.text = "posY: ${scene.entityPlayer.position.y}"
                    }
                }
            }
        )

        addGuiElement(
            GuiLabel(4f, 40f, "undefined").apply {
                onAnimateLabelListener = object : OnAnimateLabelListener {
                    override fun onAnimate(label: GuiLabel) {
                        label.text = "posZ: ${scene.entityPlayer.position.z}"
                    }
                }
            }
        )

        addGuiElement(
            GuiLabel(4f, 52f, "undefined").apply {
                onAnimateLabelListener = object : OnAnimateLabelListener {
                    override fun onAnimate(label: GuiLabel) {
                        val yaw = scene.cameraRotYaw
                        label.text = "cam Yaw: $yaw"
                    }
                }
            }
        )

        addGuiElement(
            GuiLabel(4f, 64f, "undefined").apply {
                onAnimateLabelListener = object : OnAnimateLabelListener {
                    override fun onAnimate(label: GuiLabel) {
                        val pitch = scene.cameraRotPitch
                        label.text = "cam Pitch: $pitch"
                    }
                }
            }
        )
    }

    fun switchDebuggingState() {
        this.isDebugging = !this.isDebugging
    }
}