package com.harper.spacewar.main.entity

import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.buffer.VertexFormat
import com.harper.spacewar.main.gl.tessellator.Tessellator
import com.harper.spacewar.main.scene.renderer.RenderManager
import org.joml.AABBf
import org.joml.Vector3f

abstract class Entity(private val renderManager: RenderManager) {
    val position: Vector3f = Vector3f(0f)

    var rotYaw: Float = 0f
        private set

    var rotPitch: Float = 0f
        private set

    protected var axisAlignedBox: AABBf = AABBf(0f, 0f, 0f, 1f, 1f, 1f)

    open fun create(x: Float, y: Float, z: Float) {
        setPosition(x, y, z)
    }

    open fun update(time: Float) {
        renderManager.renderEntity(this, position.x, position.y, position.z)
    }

    fun setPosition(x: Float, y: Float, z: Float) {
        this.position.set(x, y, z)
        this.updateAxisAlignedBoxPosition()
    }

    fun move(x: Float, y: Float, z: Float) {
        this.setPosition(this.position.x + x, this.position.y + y, this.position.z + z)
    }

    fun rotate(yaw: Float, pitch: Float) {
        this.rotYaw = yaw % 360f
        this.rotPitch = pitch % 360f
    }

    fun getBounds(): AABBf {
        return this.axisAlignedBox
    }

    fun drawAxisAlignedBox() {
        GlUtils.glColor(0xff0000fff)
        GlUtils.glLineWidth(2f)
        GlUtils.glDisable(GlUtils.TEXTURE_2D)
        GlUtils.glPopMatrix()
        Tessellator.instance.tessellate(GlUtils.DRAW_MODE_LINE_LOOP, VertexFormat.POSITION) {
            pos(axisAlignedBox.minX, axisAlignedBox.maxY, axisAlignedBox.minZ).completeVertex()
            pos(axisAlignedBox.maxX, axisAlignedBox.maxY, axisAlignedBox.minZ).completeVertex()
            pos(axisAlignedBox.maxX, axisAlignedBox.minY, axisAlignedBox.minZ).completeVertex()
            pos(axisAlignedBox.minX, axisAlignedBox.minY, axisAlignedBox.minZ).completeVertex()

            pos(axisAlignedBox.maxX, axisAlignedBox.maxY, axisAlignedBox.maxZ).completeVertex()
            pos(axisAlignedBox.minX, axisAlignedBox.maxY, axisAlignedBox.maxZ).completeVertex()
            pos(axisAlignedBox.minX, axisAlignedBox.minY, axisAlignedBox.maxZ).completeVertex()
            pos(axisAlignedBox.maxX, axisAlignedBox.minY, axisAlignedBox.maxZ).completeVertex()

            pos(axisAlignedBox.minX, axisAlignedBox.maxY, axisAlignedBox.maxZ).completeVertex()
            pos(axisAlignedBox.minX, axisAlignedBox.maxY, axisAlignedBox.minZ).completeVertex()
            pos(axisAlignedBox.minX, axisAlignedBox.minY, axisAlignedBox.minZ).completeVertex()
            pos(axisAlignedBox.minX, axisAlignedBox.minY, axisAlignedBox.maxZ).completeVertex()

            pos(axisAlignedBox.maxX, axisAlignedBox.maxY, axisAlignedBox.minZ).completeVertex()
            pos(axisAlignedBox.maxX, axisAlignedBox.maxY, axisAlignedBox.maxZ).completeVertex()
            pos(axisAlignedBox.maxX, axisAlignedBox.minY, axisAlignedBox.maxZ).completeVertex()
            pos(axisAlignedBox.maxX, axisAlignedBox.minY, axisAlignedBox.minZ).completeVertex()
        }
        GlUtils.glPushMatrix()
        GlUtils.glEnable(GlUtils.TEXTURE_2D)
    }

    private fun updateAxisAlignedBoxPosition() {
        this.axisAlignedBox.translate(
            this.position.x - axisAlignedBox.minX - (axisAlignedBox.maxX - axisAlignedBox.minX) / 2f,
            this.position.y - axisAlignedBox.minY,
            this.position.z - axisAlignedBox.minZ - (axisAlignedBox.maxZ - axisAlignedBox.minZ) / 2f
        )
    }
}