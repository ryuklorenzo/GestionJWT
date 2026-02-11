package ies.sequeros.dam.pmdm.gestionperifl.infraestructure.repositories


import ies.sequeros.dam.pmdm.gestionperifl.domain.repositories.IFilesRepository
import ies.sequeros.dam.pmdm.gestionperifl.domain.repositories.StorageEntity
import java.io.File
import kotlin.io.extension
import kotlin.io.writeBytes
import kotlin.text.isBlank


class FilesRepository: IFilesRepository {
    override suspend fun save(
        entity: StorageEntity,
        fileName: String,
        id: String,
        bytes: ByteArray
    ): Pair<String, String> {
        // Obtener extensión
        val extension = File(fileName).extension
        if (extension.isBlank()) {
            throw kotlin.IllegalArgumentException("Extensión no encontrada")
        }
        val nuevoNombre = "$id.$extension"
        val path=entity.path+nuevoNombre
        val file = File(path)
        file.parentFile.mkdirs()
        file.writeBytes(bytes)
        return Pair(nuevoNombre, path)
    }

    override suspend fun delete(entity: StorageEntity, fileName: String) {
        val file= File(entity.path+fileName)
        file.delete()
    }

    override fun getImagePath(entity: StorageEntity, fileName: String): String {
        if(fileName==null || fileName.isBlank()) {
            return ""
        }
        val path=entity.path+fileName
        return path
    }
}