plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)

    alias(libs.plugins.kotlin.plugin.serialization)
    application
}

group = "ies.sequeros.dam.pmdm.gestionperifl"
version = "1.0.0"
application {
    mainClass.set("ies.sequeros.dam.pmdm.gestionperifl.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
    implementation("io.ktor:ktor-server-cio:3.3.3")
    //inyección de dependencias
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)

    //modulos de ktor
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.statuspage)

    implementation(libs.ktor.server.resources)
    implementation(libs.ktor.server.double.receive)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.validator)
    implementation("io.ktor:ktor-server-cors:3.3.0")
    //jpa con hibernate
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
    implementation("org.hibernate.orm:hibernate-hikaricp:6.4.4.Final")
    implementation(libs.postgresql)
    implementation(libs.h2)
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    //json-schema
    implementation("com.networknt:json-schema-validator:1.5.8")
    // codificar contrasenyas
    implementation("org.springframework.security:spring-security-crypto:7.0.2")
    // necesita el login para codificar
    implementation("commons-logging:commons-logging:1.3.5")
    //cache de los repositorios
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
}