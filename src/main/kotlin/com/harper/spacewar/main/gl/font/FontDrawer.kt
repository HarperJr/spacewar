package com.harper.spacewar.main.gl.font

import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.buffer.BufferBuilder
import com.harper.spacewar.main.gl.buffer.VertexFormat
import com.harper.spacewar.main.gl.tessellator.Tessellator
import com.harper.spacewar.main.gl.texture.Texture
import com.harper.spacewar.main.gl.texture.TextureManager
import com.harper.spacewar.utils.FileProvider
import javax.imageio.ImageIO

class FontDrawer(private val textureManager: TextureManager) {
    private val fileProvider = FileProvider.get()
    private val charWidthList = IntArray(CHARS_COUNT)
    private val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!.,%-+/&()[] "

    private var fontTexture: Texture? = null

    fun initializeFont(fontTextureRes: String) {
        if (fontTexture != null)
            return
        this.fontTexture = textureManager.provideTexture(fontTextureRes)

        val fontImage = ImageIO.read(fileProvider.provideFile(fontTextureRes))
        val fontWidth = fontImage.width
        val fontHeight = fontImage.height
        val fontPixels = IntArray(fontWidth * fontHeight)
        fontImage.getRGB(0, 0, fontWidth, fontHeight, fontPixels, 0, fontWidth)

        val charOffsetX = fontWidth / 16
        val charOffsetY = fontHeight / 16
        val charMinOffset = 8.0f / charOffsetX.toFloat()

        for (i in 0 until CHARS_COUNT) {
            val rawOffset = i % 16
            val colOffset = i / 16

            if (i == WHITESPACE_CHAR_INDEX) {
                this.charWidthList[i] = 4
                continue
            }

            var minCharWidth: Int = charOffsetX - 1
            while (minCharWidth >= 0) {
                val i2 = rawOffset * charOffsetX + minCharWidth
                var flag1 = true

                var charDeltaOffset = 0
                while (charDeltaOffset < charOffsetY && flag1) {
                    val k2 = (colOffset * charOffsetX + charDeltaOffset) * fontWidth
                    if (fontPixels[i2 + k2] shr 24 and 255 != 0)
                        flag1 = false
                    ++charDeltaOffset
                }
                if (!flag1)
                    break
                --minCharWidth
            }

            ++minCharWidth
            this.charWidthList[i] = (0.5f + (minCharWidth.toFloat() * charMinOffset).toDouble()).toInt() + 1
        }
    }

    fun drawText(text: String, x: Float, y: Float, color: Long, scaleFactor: Float) {
        if (fontTexture == null)
            return

        GlUtils.glEnable(GlUtils.TEXTURE_2D)
        GlUtils.glEnable(GlUtils.BLEND)
        GlUtils.glBlendFuncDefault()

        GlUtils.glColor(color)

        GlUtils.glBindTexture(fontTexture!!.glTexture)
        val tessellator = Tessellator.instance
        tessellator.tessellate(GlUtils.DRAW_MODE_QUADS, VertexFormat.POSITION_TEX) {
            var charXOffset = x
            text.forEach { char ->
                val charIndex = chars.indexOf(char)

                var charYOffset = y
                when (charIndex) {
                    G_CHAR_INDEX,
                    P_CHAR_INDEX,
                    Q_CHAR_INDEX,
                    Y_CHAR_INDEX -> charYOffset += 2f * scaleFactor
                }

                drawChar(this, charXOffset, charYOffset, scaleFactor, charIndex)
                charXOffset += charWidthList[charIndex] * scaleFactor
            }
        }

        GlUtils.glDisable(GlUtils.BLEND)
        GlUtils.glDisable(GlUtils.TEXTURE_2D)
    }

    fun drawCenteredText(text: String, x: Float, y: Float, color: Long, scaleFactor: Float) {
        var textWidth = 0f
        for (char in text)
            textWidth += charWidthList[chars.indexOf(char)] * scaleFactor
        drawText(text, x - textWidth / 2f, y - 4f * scaleFactor, color, scaleFactor)
    }

    private fun drawChar(bufferBuilder: BufferBuilder, x: Float, y: Float, scaleFactor: Float, charIndex: Int) {
        val charWidth = charWidthList[charIndex]
        val charHeight = 8f
        val charWidthScaled = charWidth * scaleFactor
        val charHeightScaled = charHeight * scaleFactor
        val charOffsetX = charIndex % 16 * 8f
        val charOffsetY = charIndex / 16 * 8f
        val (fontWidth, fontHeight) = fontTexture!!
        with(bufferBuilder) {
            // top left
            pos(x, y + charHeightScaled, 0f)
                .tex(charOffsetX / fontWidth.toFloat(), (charOffsetY + charHeight) / fontHeight.toFloat())
                .completeVertex()
            // top right
            pos(x + charWidthScaled, y + charHeightScaled, 0f)
                .tex((charOffsetX + charWidth) / fontWidth.toFloat(), (charOffsetY + charHeight) / fontHeight.toFloat())
                .completeVertex()
            // bottom right
            pos(x + charWidthScaled, y, 0f)
                .tex((charOffsetX + charWidth) / fontWidth.toFloat(), charOffsetY / fontHeight.toFloat())
                .completeVertex()
            // bottom left
            pos(x, y, 0f)
                .tex(charOffsetX / fontWidth.toFloat(), charOffsetY / fontHeight.toFloat())
                .completeVertex()
        }
    }

    companion object {
        private const val CHARS_COUNT = 256
        private const val G_CHAR_INDEX = 32
        private const val P_CHAR_INDEX = 41
        private const val Q_CHAR_INDEX = 42
        private const val Y_CHAR_INDEX = 50
        private const val WHITESPACE_CHAR_INDEX = 74
    }
}