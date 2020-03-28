package com.harper.spacewar.main.entity.impl

import com.harper.spacewar.main.entity.EntityLiving
import com.harper.spacewar.main.scene.SceneInGame
import com.harper.spacewar.main.sprite.Sprite
import org.joml.AABBf

class EntityEnemy(scene: SceneInGame, maxHealth: Float, val type: Type) : EntityLiving(scene, maxHealth) {
    override val rotationSpeed: Float = 1f
    override val sprites: List<Sprite>
        get() = listOf(enemySprites[type]!!)

    private val enemySprites: MutableMap<Type, Sprite> = mutableMapOf()

    init {
        super.axisAlignedBox = AABBf(0f, 0f, 0f, 100f, 20f, 100f)
        for (type in Type.values())
            enemySprites[type] = Sprite(scene, type.spriteX, type.spriteY, 1f, 1f)
                .apply {
                    val entityHeight = axisAlignedBox.maxY - axisAlignedBox.minY
                    this.position.set(0f, -entityHeight * 1.2f, 0f)
                }
    }

    override fun create(x: Float, y: Float, z: Float) {
        super.create(x, y, z)
    }

    override fun update(time: Float) {
        super.update(time)
    }

    enum class Type(val spriteX: Float, val spriteY: Float) {
        COMMON(0f, 7f), HEAVY(1f, 7f)
    }
}