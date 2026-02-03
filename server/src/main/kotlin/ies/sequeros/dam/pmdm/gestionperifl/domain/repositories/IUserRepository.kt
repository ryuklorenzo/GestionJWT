package ies.sequeros.dam.pmdm.gestionperifl.domain.repositories

import ies.sequeros.dam.pmdm.gestionperifl.domain.entities.User
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.entities.UserJPA
import java.util.UUID

interface IUserRepository {
    //  Gestión del Perfil
    fun findById(id: UUID): User?
    fun findByEmail(email: String): User?
    fun findByUsername(username: String): User?
    fun create(user: User, password: String): User
    fun update(user: User): User
    fun delete(user: User): Unit
    fun exists(id: UUID): Boolean
    fun existsByName(name: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun existsByNameOrEmail(name: String, email: String): Boolean
    // Gestión de Seguridad
    // Estas funciones manejan hashes de contraseñas sin que el objeto User las vea
    fun updatePassword(userId: UUID, newPasswordHash: String): Boolean
    fun updateImage(user:User):User
    fun getPasswordHash(userId: UUID): String?
    fun getPasswordHash(email:String): String?
}