package ies.sequeros.dam.pmdm.gestionperifl.application.user.updateimage

import ies.sequeros.dam.pmdm.gestionperifl.application.commons.UserDto
import ies.sequeros.dam.pmdm.gestionperifl.application.exceptions.NotFoundException
import ies.sequeros.dam.pmdm.gestionperifl.domain.repositories.IFilesRepository
import ies.sequeros.dam.pmdm.gestionperifl.domain.repositories.IUserRepository
import ies.sequeros.dam.pmdm.gestionperifl.domain.repositories.StorageEntity

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class UpdateImageUserCasoDeUso(
   private val repository: IUserRepository,
   private val fileRepository: IFilesRepository
) {
    suspend operator fun invoke(id:UUID,command: UpdateImageUserCommand): UserDto =
        withContext(Dispatchers.IO) {
            //obtener el usuario
            var user = repository.findById(id) ?: throw NotFoundException("User", id.toString())
            val oldImageName = user.image
            var newImageName: String? = null

            // Si hay una nueva imagen, guardarla en el sistema de archivos
            if (command.imagen != null && command.fileName != null ) {
                val storageResult = fileRepository.save(
                    StorageEntity.USUARIO,
                    command.fileName,
                    user.id.toString(),
                    command.imagen
                )
                newImageName = storageResult.first
                user = user.copy(image = newImageName)
            }

            // Persistir en Base de Datos en caso de fallo borrar la imagen guardar
            try {
                repository.updateImage(user)
            } catch (e: Exception) {
                // Si la DB falla, borrar la imagen que acabamos de subir
                newImageName?.let { fileRepository.delete(StorageEntity.USUARIO, it) }
                throw e
            }

            // Borrar la imagen antigua si la actualización fue exitosa
            // Solo si había una imagen vieja Y es distinta a la nueva (extensión)
            if (newImageName != null &&
                oldImageName != null &&

                oldImageName != newImageName) {
                fileRepository.delete(StorageEntity.USUARIO, oldImageName)
            }

            UserDto.fromDomain(user)
        }
}