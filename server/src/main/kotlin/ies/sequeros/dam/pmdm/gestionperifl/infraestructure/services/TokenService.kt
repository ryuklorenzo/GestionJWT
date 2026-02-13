package ies.sequeros.dam.pmdm.gestionperifl.infraestructure.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import ies.sequeros.dam.pmdm.gestionperifl.application.exceptions.InvalidCredentialsException
import ies.sequeros.dam.pmdm.gestionperifl.application.services.AuthTokens
import ies.sequeros.dam.pmdm.gestionperifl.application.services.ITokenService
import ies.sequeros.dam.pmdm.gestionperifl.domain.entities.User
import ies.sequeros.dam.pmdm.gestionperifl.domain.repositories.StorageEntity
import java.util.Date
import java.util.UUID

class TokenService(
    private val secret: String,
    private val issuer: String,
    private val imageBaseUrl: String,
    private val accessTokenExpiresInMiliSecons: Long=3600 * 1000/60, //1 Hora
    private val refreshTokenExpiresMiliSecons: Long=15L * 24 * 60 * 60 * 1000 // 15 días en milisegundos
) : ITokenService {

    private val algorithm = Algorithm.HMAC256(secret)

    override fun generateAuthTokens(user: User): AuthTokens {
        val accessToken = generateAccessToken(user)
        val idToken = generateIdToken(user)
        val refreshToken = this.generateRefreshToken(user) // UUID.randomUUID().toString() // O un JWT de larga duración
        val expiresIn = accessTokenExpiresInMiliSecons/1000

        return AuthTokens(
            accessToken = accessToken,
            idToken = idToken,
            refreshToken = refreshToken,
            expiresIn = expiresIn
        )
    }

    override fun validateToken(token: String): UUID {
        try {
            val verifier = JWT.require(algorithm).withIssuer(issuer).build()
            val decodedJWT = verifier.verify(token)
            val userIdString = decodedJWT.subject
            if (userIdString == null || userIdString.isEmpty()) {
                throw InvalidCredentialsException("Invalid JWT token")
            }
            return UUID.fromString(userIdString)
        }catch (ex: Exception   ){
            throw InvalidCredentialsException("Invalid JWT token")
        }

    }

    private fun generateRefreshToken(user: User): String {
        return JWT.create()
            .withIssuer(issuer)
            .withSubject(user.id.toString())
            .withClaim("type", "refresh") // Claim extra para distinguir el tipo de token
            .withIssuedAt((Date()))
            .withExpiresAt(Date(System.currentTimeMillis() + this.refreshTokenExpiresMiliSecons)) // 1h
            .sign(algorithm)
    }
    private fun generateAccessToken(user: User): String {
        return JWT.create()
            .withIssuer(issuer)
            .withSubject(user.id.toString())
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + this.accessTokenExpiresInMiliSecons)) // 1h
            .sign(algorithm)
    }

    private fun generateIdToken(user: User): String {
        StorageEntity.USUARIO.path
        val fullImageUrl = if (user.image.isNullOrBlank()) {
            "${imageBaseUrl}/${StorageEntity.USUARIO.path}default-avatar.png"
        } else {
            "${imageBaseUrl}/${StorageEntity.USUARIO.path}${user.image}"
        }

        return JWT.create()
            .withIssuer(issuer)
            .withSubject(user.id.toString())
            .withClaim("name", user.name)
            .withClaim("email", user.email)
            .withClaim("picture", fullImageUrl) // Claim de perfil para el Front-end
            .withClaim("status",user.status.name)
            .withExpiresAt(Date(System.currentTimeMillis() + accessTokenExpiresInMiliSecons))
            .sign(algorithm)
    }
}