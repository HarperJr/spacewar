package com.harper.spacewar.main.entity.particle

import com.harper.spacewar.Color
import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.scene.Scene

abstract class EntityParticle(scene: Scene, val type: Type) : Entity(scene) {
    abstract val lifeMillis: Long
    abstract val speed: Float

    var color: Long = Color.GRAY
    var isEnded: Boolean = false
    var scale: Float = 1f
        protected set

    private val currentTimeMillis: Long
        get() = System.currentTimeMillis()
    private var prevTimeMillis: Long = currentTimeMillis

    override fun create(id: Int, x: Float, y: Float, z: Float) {
        super.create(id, x, y, z)
        this.prevTimeMillis = this.currentTimeMillis
    }

    override fun update(time: Float) {
        if (this.prevTimeMillis + lifeMillis <= this.currentTimeMillis)
            this.isEnded = true
    }

    enum class Type(val texX: Float, val texY: Float) {
        SMOKE(0f, 4f)
    }
}