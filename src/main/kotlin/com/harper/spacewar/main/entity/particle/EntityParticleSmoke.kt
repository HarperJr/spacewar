package com.harper.spacewar.main.entity.particle

import com.harper.spacewar.main.scene.Scene
import com.harper.spacewar.utils.lookRotation
import org.joml.Random
import org.joml.Vector3f
import kotlin.math.max

class EntityParticleSmoke(scene: Scene) : EntityParticle(scene, Type.SMOKE) {
    private val random: Random = Random(0xff)

    override val lifeMillis: Long
        get() = 2000L + random.nextInt(1000).toLong()
    override val speed: Float = 0.01f

    private val prevPos: Vector3f = Vector3f(0f)
    private val randomVec = Vector3f()

    override fun create(id: Int, x: Float, y: Float, z: Float) {
        super.create(id, x, y, z)
        this.randomVec.set(
            0.5f - random.nextFloat(),
            0.5f - random.nextFloat(),
            0.5f - random.nextFloat()
        ).normalize()
        this.prevPos.set(this.position)
        this.scale = 2.5f
    }

    override fun update(time: Float) {
        super.update(time)

        super.move(
            (this.position.x + this.randomVec.x - this.prevPos.x) * time * speed,
            (this.position.y + this.randomVec.y - this.prevPos.y) * time * speed,
            (this.position.z + this.randomVec.z - this.prevPos.z) * time * speed
        )

        this.rotation.set(lookRotation(camera.lookVec, camera.upVector))

        this.prevPos.set(this.position)
        this.scale = max(0f, this.scale - 0.05f * time)
    }
}