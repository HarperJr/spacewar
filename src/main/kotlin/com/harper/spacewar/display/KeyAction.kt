package com.conceptic.firefly.app.screen

import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE

/**
 * Standard key actions to be handled
 */
enum class KeyAction {
    PRESS, RELEASE;

    companion object {
        fun fromGlfw(action: Int) = when (action) {
            GLFW_RELEASE -> KeyAction.RELEASE
            GLFW_PRESS -> KeyAction.PRESS
            else -> throw IllegalStateException("Undefined action $action cannot be recognized")
        }
    }
}