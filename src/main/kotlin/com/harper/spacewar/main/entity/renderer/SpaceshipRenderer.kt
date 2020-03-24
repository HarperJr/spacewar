package com.harper.spacewar.main.entity.renderer

import com.harper.spacewar.main.Spacewar
import com.harper.spacewar.main.entity.EntitySpaceship
import com.harper.spacewar.main.resource.MeshesResources
import com.harper.spacewar.main.resource.RegistryRes

class SpaceshipRenderer(spacewar: Spacewar) : EntityRenderer<EntitySpaceship>(spacewar) {
    override val meshesResources: MeshesResources = spacewar.resourceRegistry.getModelResource(RegistryRes.SPACESHIP)
}
