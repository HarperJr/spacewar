package com.harper.spacewar.main.gl.shader.definition

import com.harper.spacewar.main.gl.shader.Shader
import com.harper.spacewar.main.gl.shader.impl.GuiShader
import kotlin.reflect.full.primaryConstructor

sealed class ShaderFormat(val name: String) {
    abstract val sourceFolder: String
    abstract val scripts: List<ShaderScript>

    abstract fun inflateShader(shaderProgram: Int): Shader

    protected inline fun <reified T : ShaderScript> script(): ShaderScript = T::class.primaryConstructor?.call(name)
        ?: throw IllegalStateException("Unable to create shader of type ${T::class.simpleName}")
}

object GuiShaderFormat : ShaderFormat("View") {
    override val sourceFolder: String = "view/"
    override val scripts: List<ShaderScript>
        get() = listOf(script<VertexShader>(), script<FragmentShader>())

    override fun inflateShader(shaderProgram: Int): Shader {
        return GuiShader(shaderProgram)
    }
}