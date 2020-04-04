package com.harper.spacewar.main.entity.impl

import com.harper.spacewar.main.ai.EnemyAI
import com.harper.spacewar.main.scene.SceneInGame

class EntityEnemy(scene: SceneInGame, maxHealth: Float) : EntitySpaceship(scene, maxHealth) {
    override val movementSpeed: Float = 0.25f
    override val rotationSpeed: Float = 0.125f

    private val enemyAi: EnemyAI = EnemyAI(this, scene)

    override fun update(time: Float) {
        super.update(time)
        this.enemyAi.update(time)
    }
}