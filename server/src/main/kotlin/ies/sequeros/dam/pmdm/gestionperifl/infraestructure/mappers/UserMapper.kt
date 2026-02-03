package ies.sequeros.dam.pmdm.gestionperifl.infraestructure.mappers

import ies.sequeros.dam.pmdm.gestionperifl.domain.entities.User
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.entities.UserJPA
import java.util.UUID

/*
Opcion 1 estilo java
 */
 object  UserMapper {

    // De base de datos a Dominio (Simplemente ignoramos el password)
    fun toDomain(jpa: UserJPA): User {
        return User(
            id = jpa.id ?: UUID.randomUUID(),
            name = jpa.name ?: "",
            email = jpa.email ?: "",
            image = jpa.image,
            status = jpa.status
        )
    }

    // De Dominio a base de datos (Recibimos el hash externamente)
    fun toJPA(domain: User, encodedPassword: String?=null): UserJPA {
        return UserJPA().apply {
            id = domain.id
            name = domain.name
            email = domain.email
            password = encodedPassword // Aquí inyectamos el password gestionado aparte
            image = domain.image
            status = domain.status
        }
    }

}
//opción 2, usando características de kotlin
fun UserJPA.toDomain(): User {
    return User(
        id = this.id ?: UUID.randomUUID(),
        name = this.name ?: "",
        email = this.email ?: "",
        image = this.image,
        status = this.status // Al ser el mismo Enum, la asignación es directa
    )
}

fun User.toJPA(password:String?=null): UserJPA {
    val jpa = UserJPA()
    jpa.id = this.id
    jpa.name = this.name
    jpa.email = this.email
    // Si se pasa el password, se actualiza. Si es null, el repositorio podría ignorarlo.
    password?.let { jpa.password = it }
    jpa.password = password
    jpa.image = this.image
    jpa.status = this.status
    return jpa
}