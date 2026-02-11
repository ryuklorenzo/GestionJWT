package ies.sequeros.dam.pmdm.gestionperifl.infraestructure.repositories

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import ies.sequeros.dam.pmdm.gestionperifl.application.exceptions.AlreadyExistsException

import ies.sequeros.dam.pmdm.gestionperifl.infraestructure.exceptions.DatabaseOperationException
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import org.hibernate.exception.ConstraintViolationException
import org.hibernate.exception.JDBCConnectionException
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import java.util.function.Function


abstract class AJPARepository<T>(protected open val emf: EntityManagerFactory,
                                  protected open val tableName: String="", protected open val isCacheEnabled: Boolean = false) {


    // Configuración de la caché: Máximo 500 elementos, expira tras 10 min sin usarse
    protected val cache: Cache<String?, Any?> = Caffeine.newBuilder()
        .maximumSize(500)
        .expireAfterAccess(10, TimeUnit.MINUTES)
        .build<String?, Any?>()


    protected fun <R> executeQueryWithCache(
        key: String?,
        action: Function<EntityManager?, R?>,
        operation: String?
    ): R? {
        // Si la caché está desactivada, vamos directo a la DB
        if (!isCacheEnabled || key == null) {
            return executeQuery(action, operation)
        }

        // Si está activada, usamos Caffeine
        return cache.get(key) { _ ->
            executeQuery<R?>(action, operation)
        } as R?
    }


    protected fun invalidateCache(key: String?) {
        cache.invalidate(key)

    }

    protected abstract fun invalidateCache(key: T)


    // Para SELECTS (Queries)
    protected fun <T> executeQuery(action: Function<EntityManager?, T?>, operation: String?): T? {
        val em: EntityManager = try {
            emf.createEntityManager()
        } catch (e: Exception) {
            throw DatabaseOperationException(this.tableName, "Error de conexión: DB inaccesible para $operation", e)
        }
        try {
            em.use { em ->
                return action.apply(em)
            }
        }catch (e: RuntimeException) {

            if(e is JDBCConnectionException){
                throw DatabaseOperationException(this.tableName, operation, e)
            }else {
                throw e
            }
        }
    }

    // Para INSERT/UPDATE/DELETE (Transacciones)
    protected fun executeTransaction(action: Consumer<EntityManager?>, operation: String?) {
        val em: EntityManager = try {
            emf.createEntityManager()
        } catch (e: Exception) {

            throw DatabaseOperationException(this.tableName, "Error de conexión: DB inaccesible para $operation", e)
        }
        em.use { em ->
            val tx = em.transaction
            try {
                tx.begin()
                action.accept(em)
                tx.commit()
            } catch (e: RuntimeException) {

                if (tx.isActive) {
                    tx.rollback()
                }
                if(e is JDBCConnectionException){
                    throw DatabaseOperationException(this.tableName, operation, e)
                }else {
                    if (e.cause is ConstraintViolationException) {
                        throw AlreadyExistsException(
                            this.tableName.toString(), operation.toString()
                        )
                    }
                }
            }

        }
    }
}

