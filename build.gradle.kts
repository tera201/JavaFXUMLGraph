plugins {
    kotlin("jvm") version "1.8.0"
    id("org.openjfx.javafxplugin") version "0.0.14"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

group = "org.tera201"
version = "0.0.2-SNAPSHOT"

val javafxVersion = "21"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    implementation("org.kordamp.ikonli:ikonli-materialdesign2-pack:12.3.1")
}

javafx {
    version = javafxVersion
    modules = listOf("javafx.controls")
}