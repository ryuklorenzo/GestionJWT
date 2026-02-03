package ies.sequeros.dam.pmdm.gestionperifl

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}