package com.harper.spacewar.main.gl.shader.definition

import com.harper.spacewar.main.gl.shader.Shader
import com.harper.spacewar.main.gl.shader.impl.*
import kotlin.reflect.full.primaryConstructor

sealed class ShaderDefinition(val name: String) {
    abstract val sourceFolder: String
    abstract val scripts: List<ShaderScript>

    abstract fun inflateShader(shaderProgram: Int): Shader

    protected inline fun <reified T : ShaderScript> script(): ShaderScript = T::class.primaryConstructor?.call(name)
        ?: throw IllegalStateException("Unable to create shader of type ${T::class.simpleName}")
}

object ModelShaderDefinition : ShaderDefinition("Model") {
    override val sourceFolder: String = "model/"
    override val scripts: List<ShaderScript>
        get() = listOf(script<VertexShader>(), script<FragmentShader>())

    override fun inflateShader(shaderProgram: Int): Shader {
        return ModelShader(shaderProgram)
    }
}

object SpriteShaderDefinition : ShaderDefinition("Sprite") {
    override val sourceFolder: String = "sprite/"
    override val scripts: List<ShaderScript>
        get() = listOf(script<VertexShader>(), script<FragmentShader>())

    override fun inflateShader(shaderProgram: Int): Shader {
        return SpriteShader(shaderProgram)
    }
}

object OutlineShaderDefinition : ShaderDefinition("Outline") {
    override val sourceFolder: String = "outline/"
    override val scripts: List<ShaderScript>
        get() = listOf(script<VertexShader>(), script<FragmentShader>())

    override fun inflateShader(shaderProgram: Int): Shader {
        return OutlineShader(shaderProgram)
    }
}

object BloomShaderDefinition : ShaderDefinition("Bloom") {
    override val sourceFolder: String = "bloom/"
    override val scripts: List<ShaderScript>
        get() = listOf(script<VertexShader>(), script<FragmentShader>())

    override fun inflateShader(shaderProgram: Int): Shader {
        return BloomShader(shaderProgram)
    }
}

object GuiShaderDefinition : ShaderDefinition("Gui") {
    override val sourceFolder: String = "gui/"
    override val scripts: List<ShaderScript>
        get() = listOf(script<VertexShader>(), script<FragmentShader>())

    override fun inflateShader(shaderProgram: Int): Shader {
        return GuiShader(shaderProgram)
    }
}