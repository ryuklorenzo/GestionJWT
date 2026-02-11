package ies.sequeros.dam.pmdm.gestionperifl.ktor_config


import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.CORS


fun Application.configureCors() {

        install(CORS) {

            // 1. Permite cualquier origen (imprescindible para desarrollo y apps móviles híbridas)


            allowHost("localhost:8081") // Cambia 3000 por el puerto de tu frontend
            allowHost("127.0.0.1:8081")
            allowHost("localhost:8082") // Cambia 3000 por el puerto de tu frontend
            allowHost("127.0.0.1:8082")
            allowHost("localhost:8083") // Cambia 3000 por el puerto de tu frontend
            allowHost("127.0.0.1:8083")
            allowMethod(HttpMethod.Options) // Vital para que el navegador "pregunte" antes

            // Indica al navegador que estas cabeceras son seguras de leer
            exposeHeader(HttpHeaders.AccessControlAllowOrigin)
            allowHeader(HttpHeaders.CacheControl)
            anyHost()
            // 2. Permite todos los métodos necesarios
            allowMethod(HttpMethod.Options)
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Put)
            allowMethod(HttpMethod.Delete)
            allowMethod(HttpMethod.Patch)

            // 3. Cabeceras estándar y personalizadas
            allowHeader(HttpHeaders.Authorization)
            allowHeader(HttpHeaders.ContentType)

            // 4. Cabeceras específicas que a veces requieren ciertos frameworks de escritorio/móvil
            allowHeader("X-Requested-With")

            // 5. Exponer cabeceras si el cliente necesita leerlas (ej. tokens en el header)
            exposeHeader(HttpHeaders.Authorization)

            // 6. Configuración de seguridad para producción
            // allowCredentials = true
            // Nota: Si activas allowCredentials, no puedes usar anyHost().
            // Tendrías que usar allowHost("tu-dominio.com")
            }

         /*   anyHost()

            // 2. Permitir todos los métodos HTTP (incluyendo PATCH para tu caso)
            allowMethod(HttpMethod.Options)
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Put)
            allowMethod(HttpMethod.Delete)
            allowMethod(HttpMethod.Patch)

            // 3. Permitir los headers necesarios para Auth y Multipart
            allowHeader(HttpHeaders.Authorization)
            allowHeader(HttpHeaders.ContentType)
            allowHeader(HttpHeaders.AccessControlAllowOrigin)

            allowHost("localhost:8081") // El puerto donde corre tu App Wasm
            allowHost("127.0.0.1:8081")

            allowHost("localhost:8082") // El puerto donde corre tu App Wasm
            allowHost("127.0.0.1:8082")





            allowCredentials = true
            allowNonSimpleContentTypes = true*/
        }


