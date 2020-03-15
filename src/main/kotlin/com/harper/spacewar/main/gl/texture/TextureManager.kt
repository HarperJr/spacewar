package com.harper.spacewar.main.gl.texture

import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.utils.FileProvider
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import java.awt.image.BufferedImage
import javax.imageio.ImageIO


class TextureManager {
    private val fileProvider = FileProvider.get()
    private val textures = mutableMapOf<String, Texture>()

    fun provideTexture(textureName: String): Texture {
        return textures[textureName] ?: createTexture(
            textureImage = ImageIO.read(fileProvider.provideFile(textureName)),
            texture = GlUtils.glGenTextures()
        )
    }

    private fun createTexture(textureImage: BufferedImage, texture: Int): Texture {
        val textureBuffer = textureImage.let { image ->
            val pixelsArray = with(IntArray(size = image.width * image.height)) {
                image.getRGB(0, 0, image.width, image.height, this, 0, image.width)
            }
            val pixelsByteBuffer = BufferUtils.createByteBuffer(pixelsArray.size * BYTES_PER_PIXEL_RGBA)
            pixelsArray.forEachIndexed { i, pixel ->
                with(pixelsByteBuffer) {
                    put(i * BYTES_PER_PIXEL_RGBA + 0, (pixel shr 0 and 255).toByte())
                    put(i * BYTES_PER_PIXEL_RGBA + 1, (pixel shr 8 and 255).toByte())
                    put(i * BYTES_PER_PIXEL_RGBA + 2, (pixel shr 16 and 255).toByte())
                    put(i * BYTES_PER_PIXEL_RGBA + 3, (pixel shr 24 and 255).toByte())
                }
            }
            pixelsByteBuffer
        }

        GlUtils.glBindTexture(texture)
        GlUtils.glTexParametriDefault()

        GL11.glTexImage2D(
            GL11.GL_TEXTURE_2D,
            0,
            GL11.GL_RGBA,
            textureImage.width,
            textureImage.height,
            0,
            GL11.GL_RGBA,
            GL11.GL_UNSIGNED_BYTE,
            textureBuffer
        )

        GlUtils.glBindTexture(0)
        textureBuffer.clear()

        return Texture(textureImage.width, textureImage.height, texture)
    }

    companion object {
        private const val BYTES_PER_PIXEL_RGBA = 4
    }
}