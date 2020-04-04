package com.harper.spacewar.main.mesh.loader

import com.harper.spacewar.main.gl.texture.Texture
import com.harper.spacewar.main.gl.texture.TextureManager
import com.harper.spacewar.main.mesh.Mesh
import com.harper.spacewar.main.mesh.MeshData
import com.harper.spacewar.main.mesh.material.Color
import com.harper.spacewar.main.mesh.material.Material
import com.harper.spacewar.utils.FileProvider
import org.joml.Vector4f
import org.lwjgl.BufferUtils
import org.lwjgl.assimp.*
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import java.nio.IntBuffer

/**
 * This is possibly would lead to a very long time routine process, should be executed at separated thread
 */
class MeshLoader(private val textureManager: TextureManager) {
    private val fileProvider: FileProvider = FileProvider.get()

    fun load(meshFileName: String): List<Mesh> {
        val meshFile = fileProvider.provideFile(MESHES_DIR + meshFileName)
        val importScene = Assimp.aiImportFile(
            meshFile.absolutePath,
            Assimp.aiProcess_OptimizeMeshes or
                    Assimp.aiProcess_FlipUVs or
                    Assimp.aiProcess_JoinIdenticalVertices
        )

        return if (importScene != null) {
            loadMeshesFromScene(importScene)
        } else throw IllegalArgumentException("Unable to create mesh $meshFileName in cause: ${Assimp.aiGetErrorString()}")
    }

    private fun loadMeshesFromScene(scene: AIScene): List<Mesh> {
        val materials = loadMaterials(scene)

        val aiMeshes = scene.mMeshes()
        return if (aiMeshes != null) {
            val meshes = mutableListOf<Mesh>()
            while (aiMeshes.hasRemaining()) {
                val aiMesh = AIMesh.create(aiMeshes.get())
                val material = materials[aiMesh.mMaterialIndex() - 1]

                val vertices = unpackVertices(aiMesh)
                val texCoords = unpackTexCoords(aiMesh, material.textures.size)
                val normals = unpackNormals(aiMesh)
                val indexes = unpackIndexes(aiMesh)

                val meshName = aiMesh.mName().dataString()
                meshes.add(
                    Mesh(
                        meshName,
                        MeshData(aiMesh.mNumVertices(), vertices, texCoords, normals, indexes),
                        material
                    )
                )
            }
            meshes
        } else emptyList()
    }

    private fun unpackIndexes(aiMesh: AIMesh): IntBuffer {
        val faceBuffer = aiMesh.mFaces()
        val indexes = mutableListOf<Int>()

        while (faceBuffer.hasRemaining()) {
            val face = faceBuffer.get()
            val indexBuffer = face.mIndices()
            while (indexBuffer.hasRemaining())
                indexes.add(indexBuffer.get())
        }

        val indexBuffer = BufferUtils.createIntBuffer(indexes.size)
        indexBuffer.put(indexes.toIntArray())
        indexBuffer.position(0)

        return indexBuffer
    }

    private fun unpackVertices(aiMesh: AIMesh): FloatArray {
        val vertexBuffer = aiMesh.mVertices()
        val vertexCount = vertexBuffer.count()
        val vertices = FloatArray(vertexCount * 3)

        var i = 0
        while (vertexBuffer.hasRemaining()) {
            val vertex = vertexBuffer.get()
            vertices[i * 3 + 0] = vertex.x()
            vertices[i * 3 + 1] = vertex.y()
            vertices[i * 3 + 2] = vertex.z()
            i++
        }

        return vertices
    }

    private fun unpackTexCoords(aiMesh: AIMesh, textureCount: Int): List<FloatArray> {
        val texCoordList = mutableListOf<FloatArray>()
        for (i in 0 until textureCount) {
            val texCoordBuffer = aiMesh.mTextureCoords(i)
            if (texCoordBuffer != null) {
                val texCoordCount = texCoordBuffer.count()
                val texCoords = FloatArray(texCoordCount * 2)

                var i = 0
                while (texCoordBuffer.hasRemaining()) {
                    val texCoord = texCoordBuffer.get()
                    texCoords[i * 2 + 0] = texCoord.x()
                    texCoords[i * 2 + 1] = texCoord.y()
                    i++
                }
                texCoordList.add(texCoords)
            }
        }

        return texCoordList
    }

    private fun unpackNormals(aiMesh: AIMesh): FloatArray {
        val normalBuffer = aiMesh.mNormals() ?: return FloatArray(0)
        val normalCount = normalBuffer.count()
        val normals = FloatArray(normalCount * 3)

        var i = 0
        while (normalBuffer.hasRemaining()) {
            val normal = normalBuffer.get()
            normals[i * 3 + 0] = normal.x()
            normals[i * 3 + 1] = normal.y()
            normals[i * 3 + 2] = normal.z()
            i++
        }

        return normals
    }

    private fun loadMaterials(scene: AIScene): List<Material> {
        val numMaterials = scene.mNumMaterials()
        val materialsPointerBuffer = scene.mMaterials() ?: return emptyList()

        val materials = mutableListOf<Material>()
        for (i in 0 until numMaterials) {
            val aiMaterial = AIMaterial.create(materialsPointerBuffer.get(i))
            val aiName = AIString.calloc()
            Assimp.aiGetMaterialString(aiMaterial, Assimp.AI_MATKEY_NAME, Assimp.AI_AISTRING, 0, aiName)

            val colors = mutableListOf<Color>()
            for (cComponent in ColorComponent.values()) {
                val color = getColor(aiMaterial, cComponent.type, cComponent.aiType)
                if (color != null)
                    colors.add(color)
            }

            val textures = mutableListOf<Material.MaterialTexture>()
            for (tComponent in TextureComponent.values()) {
                val texture = getTexture(aiMaterial, tComponent.type, tComponent.aiType)
                if (texture != null)
                    textures.add(texture)
            }

            materials.add(Material(aiName.dataString(), colors, textures))
        }

        return materials
    }

    private fun getColor(material: AIMaterial, type: Color.Type, aiType: String): Color? {
        return with(AIColor4D.create()) {
            val result = Assimp.aiGetMaterialColor(material, aiType, Assimp.aiTextureType_NONE, 0, this)
            if (result == 0) {
                Color(type, Vector4f(this.r(), this.g(), this.b(), this.a()))
            } else null
        }
    }

    private fun getTexture(material: AIMaterial, type: Texture.Type, aiType: Int): Material.MaterialTexture? {
        val texturePathPtr = AIString.calloc()
        Assimp.aiGetMaterialTexture(
            material,
            aiType,
            0,
            texturePathPtr,
            null as IntBuffer?,
            null,
            null,
            null,
            null,
            null
        )

        val texturePath = texturePathPtr.dataString()
        return if (texturePath.isNotEmpty()) {
            Material.MaterialTexture(texturePath, type)
        } else null
    }

    companion object {
        private const val MESHES_DIR = "meshes/"
    }

    enum class ColorComponent(val type: Color.Type, val aiType: String) {
        AMBIENT(Color.Type.AMBIENT, Assimp.AI_MATKEY_COLOR_AMBIENT),
        DIFFUSE(Color.Type.DIFFUSE, Assimp.AI_MATKEY_COLOR_DIFFUSE),
        SPECULAR(Color.Type.SPECULAR, Assimp.AI_MATKEY_COLOR_SPECULAR),
        EMISSIVE(Color.Type.EMISSIVE, Assimp.AI_MATKEY_COLOR_EMISSIVE)
    }

    enum class TextureComponent(val type: Texture.Type, val aiType: Int) {
        AMBIENT(Texture.Type.AMBIENT, Assimp.aiTextureType_AMBIENT),
        DIFFUSE(Texture.Type.DIFFUSE, Assimp.aiTextureType_DIFFUSE),
        SPECULAR(Texture.Type.SPECULAR, Assimp.aiTextureType_SPECULAR),
        NORMAL(Texture.Type.NORMAL, Assimp.aiTextureType_NORMALS)
    }
}