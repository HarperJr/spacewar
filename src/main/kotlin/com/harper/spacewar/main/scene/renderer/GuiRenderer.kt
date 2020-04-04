package com.harper.spacewar.main.scene.renderer

import com.harper.spacewar.main.gl.shader.definition.GuiShaderDefinition
import com.harper.spacewar.main.gl.shader.impl.GuiShader
import com.harper.spacewar.main.gl.shader.provider.ShaderProvider
import com.harper.spacewar.main.gui.GuiContainer
import com.harper.spacewar.main.resolution.ScaledResolution
import org.joml.Matrix4f

class GuiRenderer {
    private val guiShader: GuiShader = ShaderProvider.provide(GuiShaderDefinition)

    private val identityMatrix: Matrix4f = Matrix4f()
    private val projectionMatrix: Matrix4f = Matrix4f()
    private val modelMatrix: Matrix4f = Matrix4f()

    fun render(scaledResolution: ScaledResolution, guiContainer: GuiContainer) {
        this.projectionMatrix.identity()
        this.projectionMatrix.ortho(
            0f,
            scaledResolution.scaledWidth,
            scaledResolution.scaledHeight,
            0f,
            0.2f,
            1000f
        )

        this.modelMatrix.identity()
        this.modelMatrix.translate(0f, 0f, -100f)

        this.guiShader.use<GuiShader> {
            bindTexture(0)
            bindMatrices(modelMatrix, identityMatrix, projectionMatrix)
            guiContainer.render()
        }
    }
}