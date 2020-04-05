import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.11"
    application
}

group = "net.harper"
version = "1.0"

repositories {
    mavenCentral()
    jcenter()
}

val log4jVersion = "2.12.1"
dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation(fileTree("libs"))

    implementation("org.apache.logging.log4j", "log4j-api", log4jVersion)
    implementation("org.apache.logging.log4j", "log4j-core", log4jVersion)
    implementation("com.google.code.gson", "gson", "2.8.6")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}