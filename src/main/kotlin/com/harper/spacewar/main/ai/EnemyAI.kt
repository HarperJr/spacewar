package com.harper.spacewar.main.ai

import com.harper.spacewar.main.entity.impl.EntityEnemy
import com.harper.spacewar.main.entity.impl.EntityPlayer
import com.harper.spacewar.main.scene.SceneInGame
import com.harper.spacewar.utils.toDegrees
import org.joml.Vector3f

class EnemyAI(private val entity: EntityEnemy, private val scene: SceneInGame) {
    private var state: EnemyState = EnemyState.IDLE
    private val player: EntityPlayer
        get() = scene.entityPlayer

    var isReadyToMakeDecision: Boolean = false

    private val currentTimeMillis: Long
        get() = System.currentTimeMillis()
    private var prevTimeMillis = currentTimeMillis

    fun update(time: Float) {
        if (!this.isReadyToMakeDecision && this.prevTimeMillis + 200L <= this.currentTimeMillis) {
            this.prevTimeMillis = currentTimeMillis
            this.isReadyToMakeDecision = true
        }

        if (isEnemyLookingAtPlayer(player, entity))
            this.state = EnemyState.ATTACK

        if (isPlayerLookingAtEnemy(player, entity) && player.isAttacking)
            this.state = EnemyState.ATTACK

        val playerPos = player.position
        var isTooClose: Boolean
        for (e in scene.getEntities()) {
            if (e == entity) continue
            isTooClose = entity.position.distance(e.position) <= 20f
            if (isTooClose) {
                this.state = EnemyState.SAVING
                break
            }
        }

        if (entity.healthPercent <= 0.2f && isPlayerLookingAtEnemy(player, entity))
            this.state = EnemyState.SAVING

        val isInReach = this.entity.position.distance(playerPos) <= REACH_DISTANCE
        if (isInReach && this.state != EnemyState.SAVING) {
            this.state = EnemyState.ATTACK
        } else this.state = EnemyState.IDLE

        this.state.update(this, this.entity, player, time)
    }

    fun isPlayerLookingAtEnemy(player: EntityPlayer, entity: EntityEnemy): Boolean {
        val playerToEnemyVec = player.position.sub(entity.position, Vector3f()).normalize()
        return toDegrees(player.lookAt.angle(playerToEnemyVec)) % 360f <= 20f
    }

    fun isEnemyLookingAtPlayer(player: EntityPlayer, entity: EntityEnemy): Boolean {
        val playerToEnemyVec = player.position.sub(entity.position, Vector3f()).normalize()
        return toDegrees(entity.lookAt.angle(playerToEnemyVec)) % 360f <= 20f
    }

    companion object {
        private const val REACH_DISTANCE = 400f
    }
}
