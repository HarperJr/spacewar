package com.harper.spacewar.main.gl.shader

import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20

abstract class Shader(private val shaderProgram: Int) {
    protected abstract val uniforms: List<String>
    private val sharedUniforms = listOf(U_MODEL_MATRIX, U_VIEW_MATRIX, U_PROJECTION_MATRIX)

    private val matrixFloatBuffer = FloatArray(MATRIX4_USAGE)

    abstract fun bindAttributes()

    fun use(process: Shader.() -> Unit) {
        GL20.glUseProgram(shaderProgram)
        process.invoke(this)
        GL20.glUseProgram(NO_PROGRAM)
    }

    fun attachShaderScript(shaderScript: Int) {
        GL20.glAttachShader(shaderProgram, shaderScript)
    }

    fun linkShaderProgram() {
        GL20.glLinkProgram(shaderProgram)
        GL20.glValidateProgram(shaderProgram)

        if (GL20.glGetProgrami(shaderProgram, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            val info = GL20.glGetProgramInfoLog(shaderProgram, Int.MAX_VALUE)
            GL20.glDeleteProgram(shaderProgram)
            throw RuntimeException("Unable to link program $info")
        }

        bindAttributes()
        bindUniforms()
    }

    fun matrices(modelMatrix: Matrix4f, viewMatrix: Matrix4f, projMatrix: Matrix4f) {
        uniformMat4(U_MODEL_MATRIX, modelMatrix.get(matrixFloatBuffer))
        uniformMat4(U_VIEW_MATRIX, viewMatrix.get(matrixFloatBuffer))
        uniformMat4(U_PROJECTION_MATRIX, projMatrix.get(matrixFloatBuffer))
    }

    fun deleteShaderProgram() {
        GL20.glDeleteProgram(shaderProgram)
    }

    fun uniformFloat(uniform: String, float: Float) = GL20.glUniform1f(uniformLocation(uniform), float)

    fun uniformVec3(uniform: String, vector3: Vector3f) =
        GL20.glUniform3f(uniformLocation(uniform), vector3.x, vector3.y, vector3.z)

    fun uniformInt(uniform: String, int: Int) = GL20.glUniform1i(uniformLocation(uniform), int)

    fun uniformVec4(uniform: String, vector4: Vector4f) =
        GL20.glUniform4f(uniformLocation(uniform), vector4.x, vector4.y, vector4.z, vector4.w)

    fun uniformMat3(uniform: String, mat3: FloatArray) = GL20.glUniformMatrix3fv(uniformLocation(uniform), false, mat3)

    fun uniformMat4(uniform: String, mat4: FloatArray) = GL20.glUniformMatrix4fv(uniformLocation(uniform), false, mat4)

    protected fun attributeLocation(attribute: Int, attributePointer: String) {
        GL20.glBindAttribLocation(shaderProgram, attribute, attributePointer)
    }

    private fun uniformLocation(uniform: String) = uniformLocations[uniform]
        ?: throw IllegalArgumentException("Unable to find uniform $uniform in shader")

    private val uniformLocations = mutableMapOf<String, Int>()

    private fun bindUniforms() = this.apply {
        val allUniforms = sharedUniforms.union(uniforms)
        allUniforms.forEach { uniform ->
            uniformLocations[uniform] = GL20.glGetUniformLocation(shaderProgram, uniform)
        }
    }

    companion object {
        private const val NO_PROGRAM = 0
        private const val MATRIX4_USAGE = 16
        private const val U_MODEL_MATRIX = "modelMatrix"
        private const val U_VIEW_MATRIX = "viewMatrix"
        private const val U_PROJECTION_MATRIX = "projectionMatrix"
    }
}