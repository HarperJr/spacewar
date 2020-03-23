package com.harper.spacewar.display

import org.lwjgl.glfw.GLFW

/**
 * All used keys are declared here
 */
enum class Key {
    ESC, WHITESPACE, A, W, D, S, F3, UNDEFINED;

    companion object {
        fun fromGlfw(key: Int) = when (key) {
            GLFW.GLFW_KEY_ESCAPE -> ESC
            GLFW.GLFW_KEY_SPACE -> WHITESPACE
            GLFW.GLFW_KEY_A -> A
            GLFW.GLFW_KEY_W -> W
            GLFW.GLFW_KEY_D -> D
            GLFW.GLFW_KEY_S -> S
            GLFW.GLFW_KEY_F3 -> F3
            else -> UNDEFINED
        }
    }
}