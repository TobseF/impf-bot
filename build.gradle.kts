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
}

dependencies {
    shadow(gradleApi())
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")

    implementation("org.seleniumhq.selenium:selenium-java:3.141.59")
    implementation("org.seleniumhq.selenium:selenium-support:3.141.59")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.6")
    implementation("org.whispersystems:signal-service-java:2.15.3")
    implementation("com.slack.api:slack-api-client:1.7.1")
    implementation("com.slack.api:bolt:1.7.1")
    implementation("com.slack.api:bolt-servlet:1.7.1")
    implementation("org.slf4j:slf4j-simple:1.7.30")
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