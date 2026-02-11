package ies.sequeros.dam.pmdm.gestionperifl.domain.services

interface IPasswordEncoder {
    fun encode(plainText: String): String
    fun matches(plainText: String, encodedPassword: String): Boolean
}