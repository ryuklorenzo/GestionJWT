package ies.sequeros.com.dam.pmdm.vegaburguer.admin.ui

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual object IODispatchers {
    actual val io: CoroutineDispatcher= Dispatchers.IO
}