package com.harper.spacewar.main.entity.renderer

import com.harper.spacewar.main.Spacewar
import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.model.Model
import com.harper.spacewar.main.resource.MeshesResources

abstract class EntityRenderer<T : Entity>(private val spacewar: Spacewar) {
    abstract val meshesResources: MeshesResources
    private var entityModel: Model? = null
    private var isLoaded = false

    fun render(entity: T, x: Float, y: Float, z: Float) {
        this.isLoaded = meshesResources.isLoaded()
        if (this.isLoaded) {
            if (this.entityModel == null)
                this.entityModel = Model(meshesResources.get())
            this.entityModel!!.render(spacewar.camera, entity.rotYaw, entity.rotRoll, x, y, z)
        } else meshesResources.load()
    }
}
