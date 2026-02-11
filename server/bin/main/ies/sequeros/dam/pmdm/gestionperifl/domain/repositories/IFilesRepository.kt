package ies.sequeros.dam.pmdm.gestionperifl.domain.repositories

import io.ktor.utils.io.ByteReadChannel

interface IFilesRepository {
    suspend fun save(entity: StorageEntity,
                     fileName: String, id: String,   bytes: ByteArray):Pair<String,String>
    suspend fun delete(entity: StorageEntity,fileName:String)
    fun getImagePath(entity: StorageEntity,fileName:String):String
}