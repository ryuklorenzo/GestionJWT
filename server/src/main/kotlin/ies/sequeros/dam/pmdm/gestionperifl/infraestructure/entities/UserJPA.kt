package ies.sequeros.dam.pmdm.gestionperifl.infraestructure.entities

import ies.sequeros.dam.pmdm.gestionperifl.domain.entities.UserStatus
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.*



@Entity
@Table(name = "users")
@NamedQueries(

    value = [
        NamedQuery(
            name = "User.getAll",
            query = "SELECT DISTINCT u FROM  UserJPA  u"
        ),
        NamedQuery(
            name = "User.findById",
            query = "SELECT u FROM UserJPA u WHERE u.id = :id"
        ),
        NamedQuery(
            name = "User.findByEmail",
            query = "SELECT u FROM UserJPA u WHERE u.email = :email"
        ),
        NamedQuery(
            name = "User.findByName",
            query = "SELECT u FROM UserJPA u WHERE u.name = :name"
        ),
        NamedQuery(
            name = "User.existsById",
            query = "SELECT count(u) >0 FROM UserJPA u WHERE u.id = :id"
        ),
        NamedQuery(
            name = "User.existsByEmail",
            query = "SELECT count(u) >0 FROM UserJPA u WHERE u.email = :email"
        ),
        NamedQuery(
            name = "User.existsByName",
            query = "SELECT count(u) >0 FROM UserJPA u WHERE u.name = :name"
        ),
        NamedQuery(
            name = "User.existsByEmailOrName",
            query = "SELECT count(u) >0 FROM UserJPA u WHERE u.name = :name or u.email= :email"
        ),
        NamedQuery(
            name = "User.updateUser",
            query = "UPDATE UserJPA u SET u.name = :name, u.status=:status WHERE u.id = :id"
        ),
        NamedQuery(
            name = "User.updateUserImage",
            query = "UPDATE UserJPA u SET u.image = :image WHERE u.id = :id"
        )
    ]
)
open class UserJPA {
    @Id
    @Column(name = "id", nullable = false)
    open var id: UUID? = null

    @Column(name = "name", nullable = false)
    open var name: String? = null

    @Column(name = "email", nullable = false)
    open var email: String? = null

    @Column(name = "password", nullable = false)
    open var password: String? = null

    @Column(name = "image")
    open var image: String? = null


    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", columnDefinition = "user_status")

    open var status: UserStatus = UserStatus.pending
}