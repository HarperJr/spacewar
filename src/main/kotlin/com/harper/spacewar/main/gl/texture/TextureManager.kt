package com.harper.spacewar.main.gl.texture

import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.utils.FileProvider
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import java.awt.image.BufferedImage
import java.nio.ByteBuffer
import javax.imageio.ImageIO


class TextureManager {
    private val fileProvider = FileProvider.get()
    private val textures = mutableMapOf<String, Int>()

    fun bind(textureName: String) {
        val texture = textures[textureName] ?: let {
            val newTexture = GlUtils.glGenTextures()
                .also { tex -> textures[textureName] = tex }
            createTexture(
                textureImage = ImageIO.read(fileProvider.provideFile(textureName)),
                texture = newTexture
            )
        }
        GlUtils.glBindTexture(texture)
    }

    private fun createTexture(textureImage: BufferedImage, texture: Int): Int {
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

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture)

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP)

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

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0)

        textureBuffer.clear()
        return texture
    }

    companion object {
        private const val BYTES_PER_PIXEL_RGBA = 4
    }
}