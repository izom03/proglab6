
plugins {
    kotlin("jvm") version "1.8.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("java")
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.14.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:%kotlinVersion%")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("server.MainKt")
}

tasks.jar {
    manifest.attributes["Main-Class"] = "server.MainKt"
    from(
        configurations
            .runtimeClasspath
            .get()
            .map { zipTree(it) }
    )
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}