package com.harper.spacewar.main.resolution

class ScaledResolutionProvider {
    var resolution = ScaledResolution(0f, 0f)
        private set

    fun resolve(width: Int, height: Int) {
        var scaleFactor = 3

        var i = 0
        if (i == 0)
            i = 1000

        while (scaleFactor < i && width / (scaleFactor + 1) >= 320 && height / (scaleFactor + 1) >= 240)
            ++scaleFactor
        if (scaleFactor % 2 != 0 && scaleFactor != 1)
            --scaleFactor
        val scaledWidthD = width.toDouble() / scaleFactor.toDouble()
        val scaledHeightD = height.toDouble() / scaleFactor.toDouble()

        resolution = resolution.copy(scaledWidth = Math.ceil(scaledWidthD).toFloat(), scaledHeight = Math.ceil(scaledHeightD).toFloat())

    }
}
