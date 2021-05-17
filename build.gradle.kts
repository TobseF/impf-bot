import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.32"
    id("com.github.johnrengelman.shadow").version("7.0.0")

    application
}

group = "me.tobse.tool"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    shadow(gradleApi())

    implementation("org.seleniumhq.selenium:selenium-java:3.141.59")
    implementation("org.seleniumhq.selenium:selenium-support:3.141.59")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.6")
    implementation("com.slack.api:slack-api-client:1.7.1")
    implementation("org.slf4j:slf4j-simple:1.7.30")
    implementation("com.github.elbekD:kt-telegram-bot:1.3.8")
    implementation("io.github.bonigarcia:webdrivermanager:4.4.3")

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application() {
    applicationName = "ImpfBot"
    mainClass.set("de.tfr.impf.StartKt")
}

// Configure Shadow to output with normal jar file name:
tasks.named<ShadowJar>("shadowJar").configure {
    minimize()
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}