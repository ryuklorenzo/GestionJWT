package ies.sequeros.dam.pmdm.gestionperifl

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform