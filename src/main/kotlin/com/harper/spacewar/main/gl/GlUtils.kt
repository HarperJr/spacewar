package com.harper.spacewar.main.gl

import org.joml.Matrix4f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL30
import java.nio.ByteBuffer
import java.nio.IntBuffer

object GlUtils {
    const val DRAW_MODE_TRIANGLES = GL11.GL_TRIANGLES
    const val DRAW_MODE_TRIANGLES_FAN = GL11.GL_TRIANGLE_FAN
    const val DRAW_MODE_LINE_STRIP = GL11.GL_LINE_STRIP
    const val DRAW_MODE_LINE_LOOP = GL11.GL_LINE_LOOP
    const val DRAW_MODE_LINES = GL11.GL_LINES
    const val DRAW_MODE_QUADS = GL11.GL_QUADS

    const val COLOR_DEPTH_BUFFER_BIT = GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT

    const val TEXTURE_2D = GL11.GL_TEXTURE_2D
    const val TEXTURE_0 = GL13.GL_TEXTURE0

    const val DEPTH_TEST = GL11.GL_DEPTH_TEST
    const val COLOR = GL11.GL_COLOR

    const val BLEND = GL11.GL_BLEND
    const val DEPTH_ALWAYS = GL11.GL_ALWAYS
    const val DEPTH_LESS = GL11.GL_LESS

    const val STATIC_DRAW = GL15.GL_STATIC_DRAW
    const val ARRAY_BUFFER = GL15.GL_ARRAY_BUFFER

    private val array4Matrix = FloatArray(16)

    fun glColor(color: Long) {
        GL11.glColor4f(
            (color shr 24 and 255) / 255f, (color shr 16 and 255) / 255f,
            (color shr 8 and 255) / 255f, (color and 255) / 255f
        )
    }

    fun glClearColor(color: Long) {
        GL11.glClearColor(
            (color shr 24 and 255) / 255f, (color shr 16 and 255) / 255f,
            (color shr 8 and 255) / 255f, (color and 255) / 255f
        )
    }

    fun glGenBuffers(): Int {
        return GL15.glGenBuffers()
    }

    fun glDeleteBuffers(buffer: Int) {
        GL15.glDeleteBuffers(buffer)
    }

    fun glDrawArrays(mode: Int, first: Int, count: Int) {
        GL15.glDrawArrays(mode, first, count)
    }

    fun glDrawElements(mode: Int, pointer: IntBuffer) {
        GL15.glDrawElements(mode, pointer)
    }

    fun glGenTextures(): Int {
        return GL11.glGenTextures()
    }

    fun glBindTexture(texture: Int) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture)
    }

    fun glTexParametriDefault() {
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP)
    }

    fun glPopMatrix() {
        GL11.glPopMatrix()
    }

    fun glPushMatrix() {
        GL11.glPushMatrix()
    }

    fun glClear(mask: Int) {
        GL11.glClear(mask)
    }

    fun glViewport(x: Int, y: Int, width: Int, height: Int) {
        GL11.glViewport(x, y, width, height)
    }

    fun glEnable(target: Int) {
        GL11.glEnable(target)
    }

    fun glDisable(target: Int) {
        GL11.glDisable(target)
    }

    fun glBlendSeparateDefault() {
        GL15.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
    }

    fun glBlendFuncDefault() {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
    }

    fun glEnableDepthMask() {
        GL11.glDepthMask(true)
    }

    fun glDepthFunc(func: Int) {
        GL11.glDepthFunc(func)
    }

    fun glBufferData(buffer: ByteBuffer, mode: Int) {
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, mode)
    }

    fun glBindBuffer(target: Int, buffer: Int) {
        GL15.glBindBuffer(target, buffer)
    }

    fun glLoadMatrix(matrix: Matrix4f) {
        GL11.glLoadMatrixf(matrix.get(array4Matrix))
    }

    fun glLineWidth(width: Float) {
        GL11.glLineWidth(width)
    }

    fun glActiveTexture(tex: Int) {
        GL13.glActiveTexture(tex)
    }

    fun glClientActiveTexture(tex: Int) {
        GL13.glClientActiveTexture(tex)
    }

    fun glVertexAttribPointer(index: Int, size: Int, type: Int, normalized: Boolean, stride: Int, pointer: Long) {
        GL30.glVertexAttribPointer(index, size, type, normalized, stride, pointer)
    }

    fun glVertexAttribPointer(
        index: Int,
        size: Int,
        type: Int,
        normalized: Boolean,
        stride: Int,
        byteBuffer: ByteBuffer
    ) {
        GL30.glVertexAttribPointer(index, size, type, normalized, stride, byteBuffer)
    }

    fun glEnableVertexAttribArray(index: Int) {
        GL30.glEnableVertexAttribArray(index)
    }

    fun glDisableVertexAttribArray(index: Int) {
        GL30.glDisableVertexAttribArray(index)
    }

    fun glEnableCullface() {
        GL11.glEnable(GL11.GL_CULL_FACE)
        GL11.glCullFace(GL11.GL_BACK)
    }
}