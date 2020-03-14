package com.harper.spacewar.main.gl.shader.definition

import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL32
import org.lwjgl.opengl.GL40
import org.lwjgl.opengl.GL43

sealed class ShaderScript(prefix: String, postfix: String, val glShaderType: Int) {
    val name = "$prefix.$postfix.glsl"
}

/**
 * Shader for vertices
 */
class VertexShader(prefix: String) : ShaderScript(prefix, "vert", GL20.GL_VERTEX_SHADER)

/**
 * Shader for out color
 */
class FragmentShader(prefix: String) : ShaderScript(prefix, "frag", GL20.GL_FRAGMENT_SHADER)

/**
 * Shader for geometry. Since OpenGL 3.2 only
 */
class GeometryShader(prefix: String) : ShaderScript(prefix, "geom", GL32.GL_GEOMETRY_SHADER)

/**
 * Shader for tesselation. Since OpenGL 4.0 only
 */
class TesselationShader(prefix: String) : ShaderScript(prefix, "tess", GL40.GL_TESS_EVALUATION_SHADER)

/**
 * Shader for tesselation control. Since OpenGL 4.0 only
 */
class TesselationControlShader(prefix: String) : ShaderScript(prefix, "tesc", GL40.GL_TESS_CONTROL_SHADER)

/**
 * Shader for computations. Since OpenGL 4.3 only
 */
class ComputationShader(prefix: String) : ShaderScript(prefix, "comp", GL43.GL_COMPUTE_SHADER)
