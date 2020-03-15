package com.harper.spacewar.main.resolution

class ScaledResolutionProvider {
    var resolution = ScaledResolution(0f, 0f)
        private set

    fun resolve(width: Int, height: Int) {
        resolution = resolution.copy(scaledWidth = width / 2f, scaledHeight = height / 2f)
    }
}
