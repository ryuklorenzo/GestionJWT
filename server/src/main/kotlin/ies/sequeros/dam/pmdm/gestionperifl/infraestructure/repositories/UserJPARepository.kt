package ies.sequeros.dam.pmdm.gestionperifl.infraestructure.repositories

import ies.sequeros.dam.pmdm.gestionperifl.domain.entities.User
import ies.sequeros.dam.pmdm.gestionperifl.domain.repositories.IUserRepository
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.entities.UserJPA
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.exceptions.EntityNotFoundException
import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.mappers.UserMapper
import jakarta.persistence.EntityManagerFactory
import java.util.*

class UserJPARepository(override val emf: EntityManagerFactory) :
    AJPARepository<User>(emf,"user"), IUserRepository {

    override fun findById(id: UUID): User? {
        val cacheKey = this.tableName + id + "_id"

        val item: UserJPA? = executeQueryWithCache(cacheKey, { em ->
            // Usamos resultList + firstOrNull para evitar la NoResultException
            em!!.createNamedQuery("User.findById", UserJPA::class.java)
                .setParameter("id", id)
                .resultList
                .firstOrNull()
        }, "User.findById")

        return item?.let { UserMapper.toDomain(it) }

    }

    override fun findByEmail(email: String): User? {
        val cacheKey = this.tableName + email + "_email"
        val item: UserJPA? = executeQueryWithCache(cacheKey, { em ->
            em!!.createNamedQuery("User.findByEmail", UserJPA::class.java)
                .setParameter("email", email)
                .resultList
                .firstOrNull()
        }, "User.findByEmail")

        return item?.let { UserMapper.toDomain(it) }
    }

    override fun findByUsername(name: String): User? {
        val cacheKey = this.tableName + name + "_name"
        val item: UserJPA? = executeQueryWithCache(cacheKey, { em ->
            em!!.createNamedQuery("User.findByName", UserJPA::class.java)
                .setParameter("name", name)
                .resultList
                .firstOrNull()
        }, "User.findByName")

        return item?.let { UserMapper.toDomain(it) }
    }

    override fun delete(user: User) {
        executeTransaction({ em ->
            val ref: UserJPA? = em!!.getReference(UserJPA::class.java, user.id)
            em.remove(ref)
            //se limpia la cache

        }, "eliminar")
        invalidateCache(user)
    }

    override fun exists(id: UUID): Boolean {
        val cacheKey = this.tableName + id.toString() + "_exists_by_id"
        return executeQueryWithCache(
            cacheKey, { em ->
                em!!.createNamedQuery("User.existsById", Boolean::class.java)
                    .setParameter("id", id)
                    .getSingleResult()
            },
            "verificar existencia por id"
        )!!
    }

    override fun existsByName(name: String): Boolean {
        val cacheKey = this.tableName + name.toString() + "_exists_by_name"
        return executeQueryWithCache(
            cacheKey, { em ->
                em!!.createNamedQuery("User.existsByName", Boolean::class.java)
                    .setParameter("name", name)
                    .getSingleResult()
            },
            "verificar existencia por name"
        )!!
    }

    override fun existsByEmail(email: String): Boolean {
        val cacheKey = this.tableName + email.toString() + "_exists_by_email"
        return executeQueryWithCache(
            cacheKey, { em ->
                em!!.createNamedQuery("User.existsByEmail", Boolean::class.java)
                    .setParameter("email", email)
                    .getSingleResult()
            },
            "verificar existencia por email"
        )!!
    }

    override fun existsByNameOrEmail(name: String, email: String): Boolean {
        val cacheKey = this.tableName + email + "_" + name + "_exists_by_name_or:email"
        return executeQueryWithCache(
            cacheKey, { em ->
                em!!.createNamedQuery("User.existsByEmailOrName", Boolean::class.java)
                    .setParameter("email", email)
                    .setParameter("name", name)
                    .getSingleResult()
            },
            "verificar existencia por email o name"
        )!!
    }

    override fun  create(user: User, password: String): User {
        var userJPA = UserMapper.toJPA(user, password)
        executeTransaction({ em ->
            em!!.persist(userJPA)
        }, "crear")
        invalidateCache(this.tableName + "_all")
        return UserMapper.toDomain(userJPA)
    }

    override  fun update(user: User): User {
        var userJPA = UserMapper.toJPA(
            user
        )
        executeTransaction({ em ->
            em!!.createNamedQuery("User.updateUser").
            setParameter("status", user.status)
                .setParameter("name", user.name)
                .setParameter("id",user.id)
                .executeUpdate()
        }, "actualizar")
        invalidateCache(this.tableName + "_all")

        return UserMapper.toDomain(userJPA)
    }

    public override  fun updateImage(user: User): User {
        var userJPA = UserMapper.toJPA(
            user
        )
        executeTransaction({ em ->
            em!!.createNamedQuery("User.updateUserImage").
            setParameter("image", user.image)
                .setParameter("id",user.id)
                .executeUpdate()
        }, "actualizar imagen")
        invalidateCache(this.tableName + "_all")
        return UserMapper.toDomain(userJPA)
    }

    override fun updatePassword(userId: UUID, newPasswordHash: String): Boolean {
        var item: UserJPA? = null
        executeTransaction({ em ->
            item = em?.find<UserJPA>(UserJPA::class.java, userId)
            if (item == null)
                throw EntityNotFoundException("User", "getPasswordHash failed", Throwable("El ID no existe "))
            item?.password = newPasswordHash
            em!!.merge(item)

        }, "actualizar")
        return item != null
    }

    override fun getPasswordHash(userId: UUID): String? {
        var item: UserJPA? = null
        executeTransaction({ em ->
            item = em?.find<UserJPA>(UserJPA::class.java, userId)
              }, "obtener password")
        if (item == null)
            throw EntityNotFoundException("User", "getPasswordHash failed", Throwable("El ID no existe "))

        return item!!.password?: ""
    }
    override fun getPasswordHash(email: String): String? {

        val cacheKey = this.tableName + email + "_email"
        val item: UserJPA? = executeQueryWithCache(cacheKey, { em ->
            em!!.createNamedQuery("User.findByEmail", UserJPA::class.java)
                .setParameter("email", email)
                .resultList
                .firstOrNull()
        }, "User.findByEmail")
        if (item == null)
            throw EntityNotFoundException("User", "getPasswordHash failed", Throwable("El Email no existe "))
        return item.password?:null
    }

    override fun invalidateCache(key: User) {
        invalidateCache(this.tableName + "_all")
        invalidateCache(this.tableName + key.id + "_id")
        invalidateCache(this.tableName + key.email + "_email")
        invalidateCache(this.tableName + key.name + "_name")
        invalidateCache(this.tableName + key.id.toString() + "_exists_by_id")
        invalidateCache(this.tableName + key.name.toString() + "_exists_by_name")
        invalidateCache(this.tableName + key.email.toString() + "_exists_by_email")
        invalidateCache(this.tableName + key.email + "_" + key.name + "_exists_by_name_oremail")
    }
}