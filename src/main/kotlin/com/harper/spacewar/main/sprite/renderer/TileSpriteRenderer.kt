package com.harper.spacewar.main.sprite.renderer

import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.buffer.VertexFormat
import com.harper.spacewar.main.gl.shader.definition.SpriteShaderDefinition
import com.harper.spacewar.main.gl.shader.impl.SpriteShader
import com.harper.spacewar.main.gl.shader.provider.ShaderProvider
import com.harper.spacewar.main.gl.tessellator.Tessellator
import com.harper.spacewar.main.gl.texture.Texture
import com.harper.spacewar.main.scene.Camera
import com.harper.spacewar.main.scene.renderer.RenderManager
import com.harper.spacewar.main.sprite.Sprite

class TileSpriteRenderer(renderManager: RenderManager) : SpriteRenderer<Sprite>() {
    override val shader: SpriteShader = ShaderProvider.provide(SpriteShaderDefinition)
    override val texture: Texture = renderManager.textureManager.provideTexture("gui/game.png")

    override fun renderSprite(sprite: Sprite, camera: Camera, x: Float, y: Float, z: Float) {
        val spriteTexWidth = texture.width / 8f
        val spriteTexHeight = texture.height / 8f
        val spriteWidth = sprite.width * spriteTexWidth
        val spriteHeight = sprite.height * spriteTexHeight

        Tessellator.instance.tessellate(GlUtils.DRAW_MODE_QUADS, VertexFormat.POSITION_TEX) {
            // Upper left
            pos(-spriteWidth / 2f, spriteHeight / 2f, 0f).tex(
                (sprite.x * spriteTexWidth) / texture.width,
                (sprite.y * spriteTexHeight + spriteHeight) / texture.height
            ).completeVertex()
            // Upper right
            pos(spriteWidth / 2f, spriteHeight / 2f, 0f).tex(
                (sprite.x * spriteTexWidth + spriteWidth) / texture.width,
                (sprite.y * spriteTexHeight + spriteHeight) / texture.height
            ).completeVertex()
            // Bottom right
            pos(spriteWidth / 2f, -spriteHeight / 2f, 0f).tex(
                (sprite.x * spriteTexWidth + spriteWidth) / texture.width,
                (sprite.y * spriteTexHeight) / texture.height
            ).completeVertex()
            // Bottom left
            pos(-spriteWidth / 2f, -spriteHeight / 2f, 0f).tex(
                (sprite.x * spriteTexWidth) / texture.width,
                (sprite.y * spriteTexHeight) / texture.height
            ).completeVertex()
        }
    }
}