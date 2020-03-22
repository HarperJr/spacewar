package com.harper.spacewar.main.resource

import com.harper.spacewar.logging.Logger
import com.harper.spacewar.main.gl.texture.TextureManager
import com.harper.spacewar.main.model.Model

class ResourceRegistry(textureManager: TextureManager) {
    private val logger = Logger.getLogger<ResourceRegistry>()
    private val resources = mutableMapOf<RegistryRes, Resource<*>>(
        RegistryRes.SPACESHIP to ModelResource("/spaceship/spaceship.obj", textureManager)
    )

    private var isLoadingOrLoaded: Boolean = false

    fun loadResources() {
        if (this.isLoadingOrLoaded)
            return
        try {
            for ((key, res) in resources)
                res.load()
            this.isLoadingOrLoaded = true
        } catch (ex: Exception) {
            logger.error(ex)
        }
    }

    fun getModelResource(registryRes: RegistryRes): ModelResource {
        return getResource<Model>(registryRes) as ModelResource
    }

    private fun <T> getResource(registryRes: RegistryRes): Resource<T> {
        return resources[registryRes] as Resource<T>
    }

    fun areResourcesLoaded(): Boolean {
        var loaded = true
        for ((key, res) in resources) {
            if (!res.isLoaded()) {
                loaded = false
                break
            }
        }
        return loaded
    }
}

enum class RegistryRes {
    SPACESHIP
}