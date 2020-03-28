package com.harper.spacewar.main.damage

import com.harper.spacewar.main.entity.EntityLiving

interface DamageSource {
    fun onDamage(entity: EntityLiving)

    companion object {
        val MISSILE = object : DamageSource {
            override fun onDamage(entity: EntityLiving) {
                entity.health -= 0.2f
            }
        }
    }
}
