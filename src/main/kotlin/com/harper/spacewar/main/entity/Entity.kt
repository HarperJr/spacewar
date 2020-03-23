package com.harper.spacewar.main.entity

import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.buffer.VertexFormat
import com.harper.spacewar.main.gl.tessellator.Tessellator
import com.harper.spacewar.main.scene.renderer.RenderManager
import org.joml.AABBf
import org.joml.Vector3f

abstract class Entity(private val renderManager: RenderManager) {
    private val position: Vector3f = Vector3f(0f)

    var rotYaw: Float = 0f
        private set

    var rotRoll: Float = 0f
        private set

    protected var axisAlignedBox: AABBf = AABBf(0f, 0f, 0f, 1f, 1f, 1f)

    fun create(x: Float, y: Float, z: Float) {
        setPosition(x, y, z)
    }

    fun update(time: Float) {
        renderManager.renderEntity(this, position.x, position.y, position.z)
    }

    fun setPosition(x: Float, y: Float, z: Float) {
        this.position.set(x, y, z)
        this.setAxisAlignedBoxPosition(x, y, z)
    }

    fun move(x: Float, y: Float, z: Float) {
        this.setPosition(this.position.x + x, this.position.y + y, this.position.z + z)
    }

    fun rotate(yaw: Float, roll: Float) {
        this.rotYaw = yaw
        this.rotRoll = roll
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

    private fun setAxisAlignedBoxPosition(x: Float, y: Float, z: Float) {
        this.axisAlignedBox.translate(
            x - axisAlignedBox.minX - axisAlignedBox.maxX / 2f,
            y - axisAlignedBox.minY,
            z - axisAlignedBox.minZ - axisAlignedBox.maxZ / 2f
        )
    }
}