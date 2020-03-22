package com.harper.spacewar.main.entity.renderer

import com.harper.spacewar.CameraSource
import com.harper.spacewar.main.Spacewar
import com.harper.spacewar.main.entity.Entity
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.buffer.VertexFormat
import com.harper.spacewar.main.resource.ModelResource

abstract class EntityRenderer<T : Entity>(private val spacewar: Spacewar) {
    abstract val modelResource: ModelResource
    private val camera: CameraSource = spacewar.camera
    private var isModelLoaded = false

    fun render(entity: T, x: Float, y: Float, z: Float) {
        this.isModelLoaded = modelResource.isLoaded()
        if (this.isModelLoaded) {
            modelResource.get().render(camera, entity.rotYaw, entity.rotRoll, x, y, z)
        } else modelResource.load()
    }
}
