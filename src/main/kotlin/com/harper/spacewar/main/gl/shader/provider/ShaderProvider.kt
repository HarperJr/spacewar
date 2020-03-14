package com.harper.spacewar.main.gl.shader.provider

import com.harper.spacewar.main.gl.shader.Shader
import com.harper.spacewar.main.gl.shader.definition.ShaderFormat
import com.harper.spacewar.utils.FileProvider
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20

object ShaderProvider {
    private const val SHADERS_DIR = "/shaders"
    private val fileProvider = FileProvider.get()

    @Suppress("UNCHECKED_CAST")
    fun <T : Shader> provide(shaderFormat: ShaderFormat): T {
        val shader = shaderFormat.inflateShader(GL20.glCreateProgram())
        return kotlin.runCatching {
            shaderFormat.scripts.map { shaderScript ->
                val shaderScriptContent =
                    fileProvider.provideFileContent(SHADERS_DIR + shaderFormat.sourceFolder + shaderScript.name)
                provide(shaderScript.glShaderType, String(shaderScriptContent))
                    .also { shader.attachShaderScript(it) }
            }
            return@runCatching shader
        }.onSuccess { shader.linkShaderProgram() }
            .getOrThrow() as T
    }

    private fun provide(type: Int, content: String): Int {
        val shader = GL20.glCreateShader(type)
        GL20.glShaderSource(shader, content)
        GL20.glCompileShader(shader)

        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            val info = GL20.glGetShaderInfoLog(shader, Int.MAX_VALUE)
            GL20.glDeleteShader(shader)
            throw RuntimeException("Unable to compile shader $info")
        }

        return shader
    }
}