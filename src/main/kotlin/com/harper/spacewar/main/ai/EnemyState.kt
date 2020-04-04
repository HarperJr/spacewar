package com.harper.spacewar.main.ai

import com.harper.spacewar.main.entity.impl.EntityEnemy
import com.harper.spacewar.main.entity.impl.EntityPlayer
import com.harper.spacewar.utils.orthoNormalize
import org.joml.Random
import org.joml.Vector3f

abstract class EnemyState(private val name: String) {
    abstract fun update(ai: EnemyAI, entity: EntityEnemy, player: EntityPlayer, time: Float)

    protected fun makeDecision(ai: EnemyAI, decision: () -> Boolean) {
        ai.isReadyToMakeDecision = !decision.invoke()
    }

    protected fun getDirectionToPlayer(entity: EntityEnemy, player: EntityPlayer): Vector3f {
        val playerPos = player.position
        return playerPos.sub(entity.position, Vector3f()).normalize()
    }

    override fun equals(other: Any?): Boolean {
        return (other as? EnemyState)?.name == this.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    companion object {
        val IDLE = EnemyStateIdle("Idle")
        val SAVING = EnemyStateSaving("Saving")
        val ATTACK = EnemyStateAttack("Attack")
    }
}

class EnemyStateIdle(name: String) : EnemyState(name) {
    private val rand: Random = Random(0xff)

    override fun update(ai: EnemyAI, entity: EntityEnemy, player: EntityPlayer, time: Float) {
        val directionToPlayer = getDirectionToPlayer(entity, player)

        val angleBetween = directionToPlayer.angle(entity.lookAt)
        val nextCoord = if (angleBetween >= 35f) {
            Vector3f(
                (directionToPlayer.x - entity.lookAt.x) * time,
                (directionToPlayer.y - entity.lookAt.y) * time,
                (directionToPlayer.z - entity.lookAt.z) * time
            )
        } else Vector3f((0.5f - rand.nextFloat()), (0.5f - rand.nextFloat()), (0.5f - rand.nextFloat())).div(10f)
        if (ai.isReadyToMakeDecision) {
            super.makeDecision(ai) {
                val lookAt = Vector3f(
                    entity.lookAt.x + nextCoord.x,
                    entity.lookAt.y + nextCoord.y,
                    entity.lookAt.z + nextCoord.z
                ).normalize()
                entity.lookAt(time, lookAt)
                return@makeDecision true
            }
        }
    }
}

class EnemyStateAttack(name: String) : EnemyState(name) {
    private val currentTimeMillis: Long
        get() = System.currentTimeMillis()
    private var prevTimeMillis = currentTimeMillis

    override fun update(ai: EnemyAI, entity: EntityEnemy, player: EntityPlayer, time: Float) {
        if (ai.isReadyToMakeDecision)
            makeDecision(ai) {
                val directionToPlayer = getDirectionToPlayer(entity, player)
                entity.lookAt(time, directionToPlayer)

                if (prevTimeMillis + SHOT_THRESHOLD <= currentTimeMillis) {
                    this.prevTimeMillis = currentTimeMillis
                    entity.makeShot()
                }

                return@makeDecision ai.isPlayerLookingAtEnemy(player, entity)
            }
    }

    companion object {
        private const val SHOT_THRESHOLD = 1000L
    }
}

class EnemyStateSaving(name: String) : EnemyState(name) {
    override fun update(ai: EnemyAI, entity: EntityEnemy, player: EntityPlayer, time: Float) {
        if (ai.isReadyToMakeDecision)
            makeDecision(ai) {
                val directionToPlayer = getDirectionToPlayer(entity, player)
                val upVec = orthoNormalize(Vector3f(0f, 1f, 0f), directionToPlayer)
                val rightVec = directionToPlayer.cross(upVec, Vector3f()).normalize()
                entity.lookAt(time, rightVec)

                true
            }
    }
}