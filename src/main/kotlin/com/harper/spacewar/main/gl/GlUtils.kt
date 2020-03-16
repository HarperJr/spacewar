package com.harper.spacewar.main.gl

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import java.nio.ByteBuffer
import java.nio.IntBuffer

object GlUtils {
    const val PROJECTION = GL11.GL_PROJECTION
    const val MODELVIEW = GL11.GL_MODELVIEW
    const val DRAW_MODE_TRIANGLES = GL11.GL_TRIANGLES
    const val DRAW_MODE_TRIANGLES_FAN = GL11.GL_TRIANGLE_FAN
    const val DRAW_MODE_QUADS = GL11.GL_QUADS
    const val COLOR_DEPTH_BUFFER_BIT = GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT

    const val TEXTURE_2D = GL11.GL_TEXTURE_2D
    const val DEPTH_TEST = GL11.GL_DEPTH_TEST

    const val BLEND = GL11.GL_BLEND
    const val DEPTH_ALWAYS = GL11.GL_ALWAYS

    const val STATIC_DRAW = GL15.GL_STATIC_DRAW
    const val ARRAY_BUFFER = GL15.GL_ARRAY_BUFFER

    fun glTranslatef(x: Float, y: Float, z: Float) {
        GL11.glTranslatef(x, y, z)
    }

    fun glRotatef(a: Float, x: Float, y: Float, z: Float) {
        GL11.glRotatef(a, x, y, z)
    }

    fun glScalef(x: Float, y: Float, z: Float) {
        GL11.glScalef(x, y, z)
    }

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

    fun glEnableClientState(state: Int) {
        GL15.glEnableClientState(state)
    }

    fun glDisableClientState(state: Int) {
        GL15.glDisableClientState(state)
    }

    fun glVertexPointer(size: Int, type: Int, stride: Int, pointer: ByteBuffer) {
        GL15.glVertexPointer(size, type, stride, pointer)
    }

    fun glVertexPointer(size: Int, type: Int, stride: Int, pointer: Long) {
        GL15.glVertexPointer(size, type, stride, pointer)
    }

    fun glTexCoordPointer(size: Int, type: Int, stride: Int, pointer: ByteBuffer) {
        GL15.glTexCoordPointer(size, type, stride, pointer)
    }

    fun glTexCoordPointer(size: Int, type: Int, stride: Int, pointer: Long) {
        GL15.glTexCoordPointer(size, type, stride, pointer)
    }

    fun glColorPointer(size: Int, type: Int, stride: Int, pointer: ByteBuffer) {
        GL15.glColorPointer(size, type, stride, pointer)
    }

    fun glColorPointer(size: Int, type: Int, stride: Int, pointer: Long) {
        GL15.glColorPointer(size, type, stride, pointer)
    }

    fun glNormalPointer(type: Int, stride: Int, pointer: ByteBuffer) {
        GL15.glNormalPointer(type, stride, pointer)
    }

    fun glNormalPointer(type: Int, stride: Int, pointer: Long) {
        GL15.glNormalPointer(type, stride, pointer)
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

    fun glMatrixMode(mode: Int) {
        GL11.glMatrixMode(mode)
    }

    fun glLoadIdentity() {
        GL11.glLoadIdentity()
    }

    fun glOrtho(left: Double, right: Double, top: Double, bottom: Double, near: Double, far: Double) {
        GL11.glOrtho(left, right, top, bottom, near, far)
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
}