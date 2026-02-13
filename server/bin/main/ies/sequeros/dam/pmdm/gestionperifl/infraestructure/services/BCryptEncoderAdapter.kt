package ies.sequeros.dam.pmdm.gestionperifl.infraestructure.services

import ies.sequeros.dam.pmdm.gestionperifl.domain.services.IPasswordEncoder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import kotlin.toString

class BCryptEncoderAdapter : IPasswordEncoder {
    private val encoder = BCryptPasswordEncoder()
    override fun encode(plainText: String): String {
        return encoder.encode(plainText).toString()
    }
    override fun matches(plainText: String, encodedPassword: String): Boolean {
        return encoder.matches(plainText,encodedPassword);
    }
}