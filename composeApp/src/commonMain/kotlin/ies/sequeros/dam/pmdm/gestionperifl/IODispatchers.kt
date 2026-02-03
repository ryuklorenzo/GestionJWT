package ies.sequeros.dam.pmdm.gestionperifl

import kotlinx.coroutines.CoroutineDispatcher

expect object IODispatchers {
    val io: CoroutineDispatcher
}