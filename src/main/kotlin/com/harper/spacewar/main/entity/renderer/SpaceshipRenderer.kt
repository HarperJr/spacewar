package com.harper.spacewar.main.entity.renderer

import com.harper.spacewar.main.Spacewar
import com.harper.spacewar.main.entity.SpaceshipEntity
import com.harper.spacewar.main.resource.ModelResource
import com.harper.spacewar.main.resource.RegistryRes

class SpaceshipRenderer(spacewar: Spacewar) : EntityRenderer<SpaceshipEntity>(spacewar) {
    override val modelResource: ModelResource = spacewar.resourceRegistry.getModelResource(RegistryRes.SPACESHIP)
}
