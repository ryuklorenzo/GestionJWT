package ies.sequeros.dam.pmdm.gestionperifl.application.user.updateimage

import kotlinx.serialization.Serializable

@Serializable
data class UpdateImageUserCommand ( //val id:String,
    val id:String,
    val fileName:String, // val imagen64:String,

    val imagen: ByteArray,
    )
