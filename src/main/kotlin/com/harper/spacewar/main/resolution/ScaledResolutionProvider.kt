package com.harper.spacewar.main.resolution

class ScaledResolutionProvider {
    var resolution = ScaledResolution(0f, 0f)
        private set

    fun resolve(width: Int, height: Int) {
        resolution = resolution.copy(scaledWidth = width * 1f, scaledHeight = height * 1f)
    }
}
