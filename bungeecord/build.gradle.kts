plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.0.0"
    //id("com.android.application") version "7.3.0"
    //id("com.android.application") version "7.4.2"
    //id("com.android.library") version "8.0.0" apply false
    //id("org.jetbrains.kotlin.android") version "1.5.31" apply false
    //id("com.android.library") version "7.4.0"
}

group = "com.github.dominik48n.party"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}
/*
android {
    buildTypes {
        getByName("release") {
            // Enables code shrinking, obfuscation, and optimization for only
            // your project's release build type. Make sure to use a build
            // variant with `isDebuggable=false`.
            isMinifyEnabled = true

            // Enables resource shrinking, which is performed by the
            // Android Gradle plugin.
            isShrinkResources = true

            // Includes the default ProGuard rules files that are packaged with
            // the Android Gradle plugin. To learn more, go to the section about
            // R8 configuration files.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
*/

dependencies {
    compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")

    implementation("net.kyori:adventure-api:4.13.1")
    implementation("net.kyori:adventure-text-minimessage:4.13.1")
    implementation("net.kyori:adventure-platform-bungeecord:4.3.0")
    implementation("org.jetbrains:annotations:24.0.1")
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("org.slf4j:slf4j-simple:1.7.36")
    implementation("redis.clients:jedis:4.3.2")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation(project(":common"))
    implementation(project(":api"))

    //implementation("net.sf.proguard:proguard-base:7.1.1")

}

tasks.shadowJar {
    relocate("net.kyori", "${project.group}.libs.kyori")
    relocate("org.jetbrains", "${project.group}.libs.jetbrains")
    relocate("redis.clients", "${project.group}.libs.redis")
    relocate("org.apache.commons.pool2", "${project.group}.libs.commons.pool2")
    relocate("org.intellij.lang", "${project.group}.libs.intellij.lang")
    relocate("org.json", "${project.group}.libs.json")
    relocate("org.slf4j", "${project.group}.libs.slf4j")
    relocate("com.fasterxml.jackson", "${project.group}.libs.jackson")
    relocate("com.google.gson", "${project.group}.libs.gson")
}

tasks.processResources {
    filesMatching("bungee.yml") {
        expand(mapOf("version" to rootProject.version))
    }
}
