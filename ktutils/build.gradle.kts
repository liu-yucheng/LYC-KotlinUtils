/** Gradle build Kotlin script. */

// Copyright 2022 Yucheng Liu. Apache License Version 2.0.
// Apache License Version 2.0 copy: http://www.apache.org/licenses/LICENSE-2.0

import org.jetbrains.compose.compose
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "lyc"
version = "1.5.0"
val jarBaseName = "ktutils"

// Add version.properties to the JAR resources

tasks.processResources.configure {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filesMatching("versions.properties") { expand(properties) }

    doLast {
        copy {
            from(
                file(rootDir.resolve("README-Assets/Open-Source-Licenses.txt")),
                file(rootDir.resolve("LICENSE"))
            ) // end from

            into(buildDir.resolve("resources/main/licenses"))

            rename { name: String ->
                when (name) {
                    "Open-Source-Licenses.txt" -> "open_source_licenses.txt"
                    "LICENSE" -> "license.txt"
                    else -> name
                } // end when
            } // end rename
        } // end copy
    } // end doLast
} // end tasks

// End; Find compilation target

val osName = System.getProperty("os.name")

val targetOS = when {
    osName == "Mac OS X" -> "macos"
    osName.startsWith("Win") -> "windows"
    osName.startsWith("Linux") -> "linux"
    else -> error("Unknown OS: $osName")
} // end val

val archName = System.getProperty("os.arch")

val targetArch = when (archName) {
    "x86_64", "amd64" -> "x64"
    "aarch64" -> "arm64"
    else -> error("Unknown arch: $archName")
} // end val

val target = "$targetOS-$targetArch"

// End

plugins {
    kotlin("jvm").version("1.6.10")
    id("org.jetbrains.compose").version("1.1.0")
    id("org.jetbrains.dokka").version("1.6.10")
    idea
} // end plugins

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
} // end repositories

dependencies {
    // Kotlin

    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    api("org.jetbrains.kotlin:kotlin-stdlib")

    // End; Kotlin tests

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    // End; Compose Multiplatform

    implementation(compose.desktop.currentOs)
    // implementation(compose.preview)
    // implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1") // Not desktop-compatible
    api(compose.desktop.currentOs)
    // api(compose.preview)

    // End; Kotlin Dokka

    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.6.10")
    dokkaJavadocPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.6.10")
    dokkaGfmPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.6.10")

    // End; Decompose

    implementation("com.arkivanov.decompose:decompose:0.7.0")
    api("com.arkivanov.decompose:decompose:0.7.0")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:0.7.0")
    api("com.arkivanov.decompose:extensions-compose-jetbrains:0.7.0")

    // End; FlatLaf

    implementation("com.formdev:flatlaf:2.3")
    api("com.formdev:flatlaf:2.3")
    implementation("com.formdev:flatlaf-intellij-themes:2.3")
    api("com.formdev:flatlaf-intellij-themes:2.3")

    // End; Deep Java Library

    implementation("ai.djl:api:0.17.0")
    // implementation("org.apache.commons:commons-compress:1.2.1")
    api("ai.djl:api:0.17.0")

    // End; Guava

    implementation("com.google.guava:guava:31.1-jre")
    api("com.google.guava:guava:31.1-jre")

    // End; Gson

    implementation("com.google.code.gson:gson:2.9.0")
    api("com.google.code.gson:gson:2.9.0")

    // End
} // end dependencies

val mainClassName = "lyc.ktutils.exes.MainKt"

compose.desktop {
    application {
        mainClass = mainClassName
        // jvmArgs("-Dapple.awt.application.appearance=system") // Follow system theme
    } // end application
} // end compose

buildscript {
    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    dependencies {
        // ProGuard

        classpath("com.guardsquare:proguard-gradle:7.2.2")

        // End; Compose Multiplatform

        // classpath("org.jetbrains.compose:compose-gradle-plugin:1.1.0-preview-images")
        // classpath(kotlin("gradle-plugin", version = "1.6.10"))

        // End
    }
} // end buildscript

tasks.withType<KotlinCompile>().configureEach {
    // dependsOn(tasks.processResources.get())
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    // Opt-in Compose Multiplatform experimental features
} // end tasks

tasks.jar {
    manifest.attributes["Main-Class"] = mainClassName
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    dependsOn(configurations.runtimeClasspath)
    dependsOn(configurations.compileClasspath)
    from(sourceSets.main.get().output)

    from({
        configurations.runtimeClasspath.get().filter {
            it.name.endsWith(".jar")
        }.map {
            zipTree(it)
        } // end configurations
    }) // end from

    from({
        configurations.compileClasspath.get().filter {
            it.name.endsWith(".jar")
        }.map {
            zipTree(it)
        } // end configurations
    }) // end from
} // end tasks

tasks.register<proguard.gradle.ProGuardTask>("obfus") {
    dependsOn(tasks.jar.get())

    val obfusPath = buildDir.resolve("libs-obfus").path
    project.file(obfusPath).mkdirs()

    configuration("proguard-configs.txt")
    injars(tasks.named("jar"))
    outjars("$obfusPath/$jarBaseName-obfus-$version.jar")

    printseeds("$obfusPath/$jarBaseName-seeds.txt")
    // printusage("$obfusPath/$jarBaseName-usage.txt")
    printmapping("$obfusPath/$jarBaseName-mapping.txt")
    printconfiguration("$obfusPath/$jarBaseName-configuration.txt")

    // Include library JARs

    val javaHome = System.getProperty("java.home")

    if (System.getProperty("java.version").startsWith("1.")) {
        // Handle pre-Java-8 (including Java-8) runtime
        libraryjars("$javaHome/lib/rt.jar")
    } else {
        // Handle post-Java-9 runtime
        libraryjars(mapOf("jarfilter" to "!**.jar", "filter" to "!module-info.class"), "$javaHome/jmods")
    } // end if

    val runtimeClasspath = configurations.runtimeClasspath.get().asIterable()
    val compileClasspath = configurations.compileClasspath.get().asIterable()
    val classpath = ArrayList<File>()

    for (elem in runtimeClasspath) {
        if (elem !in classpath) {
            classpath.add(elem)
        } // end if
    } // end for

    for (elem in compileClasspath) {
        if (elem !in classpath) {
            classpath.add(elem)
        } // end if
    } // end for

    libraryjars(classpath)

    // End Include library JARs
} // end tasks

tasks.withType<DokkaTask>().configureEach {
    // custom output directory
    outputDirectory.set(buildDir.resolve("dokka"))

    dokkaSourceSets {
        configureEach {
            includes.from("Packages.md")
        } // end configureEach
    } // end dokkaSourceSets
} // end tasks
