import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.plugin)
    alias(libs.plugins.detekt)
    idea
}

group = "com.serranofp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.ktor.serialization.json)

    implementation(libs.arrow.core)
    implementation(libs.arrow.fx.coroutines)
    implementation(libs.arrow.suspendApp)

    implementation(libs.geoip2)

    implementation(libs.slf4j.simple)

    detektPlugins(libs.detekt.formatting)
    detektPlugins("com.wolt.arrow.detekt:rules:latest.release")
    detektPlugins("io.nlopez.compose.rules:detekt:latest.release")

    testImplementation(libs.turbine)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotest.runner)
}

tasks.withType<KotlinCompilationTask<*>>().configureEach {
    compilerOptions{
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "WeatherApp"
            packageVersion = "1.0.0"
        }
    }
}

detekt {
    buildUponDefaultConfig = true
    autoCorrect = true
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
