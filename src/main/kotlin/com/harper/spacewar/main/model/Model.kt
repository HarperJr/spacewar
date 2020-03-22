package com.harper.spacewar.main.model

import com.harper.spacewar.CameraSource
import com.harper.spacewar.main.gl.GlUtils
import com.harper.spacewar.main.gl.buffer.VertexFormat
import com.harper.spacewar.main.gl.shader.definition.ModelShaderDefinition
import com.harper.spacewar.main.gl.shader.impl.ModelShader
import com.harper.spacewar.main.gl.shader.provider.ShaderProvider
import com.harper.spacewar.main.mesh.Mesh
import com.harper.spacewar.main.mesh.MeshRenderer
import org.joml.Matrix4f

class Model(meshes: List<Mesh>) {
    private val modelMatrix: Matrix4f = Matrix4f()
    private val innerMeshes: List<Mesh> = meshes
    private val shader: ModelShader = ShaderProvider.provide(ModelShaderDefinition)
    private val renderers: Array<MeshRenderer> = Array(innerMeshes.size) { next ->
        MeshRenderer(innerMeshes[next])
    }

    fun render(camera: CameraSource, yaw: Float, roll: Float, x: Float, y: Float, z: Float) {
        this.modelMatrix.identity()

        GlUtils.glEnable(GlUtils.DEPTH_TEST)
        GlUtils.glDepthFunc(GlUtils.DEPTH_LESS)
        GlUtils.glEnableDepthMask()
        shader.use {
            bindMatrices(getModelMatrix(yaw, roll, x, y, z), camera.view, camera.projection)
            for (renderer in renderers)
                renderer.render(VertexFormat.OPAQUE_MODEL_UNTEXTURED)
        }

        GlUtils.glDisable(GlUtils.DEPTH_TEST)
    }

    private fun getModelMatrix(yaw: Float, roll: Float, x: Float, y: Float, z: Float): Matrix4f {
        return modelMatrix.apply {
            translate(x, y, z)
            rotateX(yaw / 180f * Math.PI.toFloat())
            rotateY(roll / 180f * Math.PI.toFloat())
        }
    }
}