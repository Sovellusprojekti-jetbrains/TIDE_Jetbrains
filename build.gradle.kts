import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*

val riderPlatformVersionProp = prop("riderPlatformVersion")
val ideaPlatformVersionProp = prop("ideaPlatformVersion")
val pluginSinceBuildProp = prop("pluginSinceBuild")
val pluginUntilBuildProp = prop("pluginUntilBuild")
val versionProp = prop("pluginVersion")
val projectType = System.getenv("IDE_TYPE") ?: "IC"

abstract class GeneratePluginInfo : DefaultTask() {
    @get:Input
    abstract val pluginVersion: Property<String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        val file = outputDir.get().file("java/com/actions/PluginInfo.java").asFile
        file.parentFile.mkdirs()
        file.writeText(
            """
            package com.actions;

            public class PluginInfo {
                public static final String VERSION = "${pluginVersion.get()}";
            }

            """.trimIndent(),
        )
    }
}

val runIdeForUiTests by intellijPlatformTesting.runIde.registering {
    task {
        jvmArgumentProviders += CommandLineArgumentProvider {
            listOf(
                "-Drobot-server.port=8082",
                "-Dide.mac.message.dialogs.as.sheets=false",
                "-Djb.privacy.policy.text=<!--999.999-->",
                "-Djb.consents.confirmation.enabled=false",
            )
        }
    }
    plugins {
        robotServerPlugin()
    }
}

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.0.20"
    // TODO: This platform version is getting old but tests fail with newer versions as of April 2025.
    id("org.jetbrains.intellij.platform") version "2.5.0"
}

group = "org.jyu"
version = versionProp

intellijPlatform {
    pluginConfiguration {
        name = "TIDE"
    }
}

apply {
    plugin("idea")
    plugin("java")
    plugin("org.jetbrains.intellij.platform")
}

repositories {
    mavenCentral()
    maven("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies")
    intellijPlatform {
        defaultRepositories()
    }
}

val mockitoAgent = configurations.create("mockitoAgent")
dependencies {
    intellijPlatform {
        if (projectType == "RD") {
            create("RD", riderPlatformVersionProp, useInstaller = false)
        } else {
            create("IC", ideaPlatformVersionProp, useInstaller = false)
            bundledPlugins("com.intellij.java", "JUnit")
        }
    }
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.12.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.12.1")
    implementation("com.google.code.gson:gson:2.12.1")
    testImplementation("com.intellij.remoterobot:remote-robot:0.11.23")
    testImplementation("com.intellij.remoterobot:remote-fixtures:0.11.23")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.mockito:mockito-core:5.16.1")
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }

    patchPluginXml {
        sinceBuild.set(pluginSinceBuildProp)
        if (pluginUntilBuildProp.isNotEmpty()) {
            untilBuild.set(pluginUntilBuildProp)
        }
    }
}

    /*
    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }*/

tasks.test {
    useJUnitPlatform()

    jvmArgs("--add-javaagent=${configurations.testRuntimeClasspath.get().find { it.name.contains("mockito") }?.absolutePath}")
}

// From https://gitlab.jyu.fi/tie/tools/comtest.intellij/-/blob/master/build.gradle.kts
fun prop(key: String) =
    extra.properties[key] as? String
        ?: error("Property `$key` is not defined in gradle.properties")

val generatePluginInfo = tasks.register<GeneratePluginInfo>("generatePluginInfo") {
    pluginVersion.set(
        project.findProperty("pluginVersion")?.toString()
            ?: error("Missing 'pluginVersion' property."),
    )
    outputDir.set(layout.buildDirectory.dir("generated/sources/version/java"))
}

// Tell the compiler to include the generated source dir
sourceSets["main"].java.srcDir("build/generated/sources/version")

// Make sure Java compile step depends on this
tasks.named<JavaCompile>("compileJava") {
    dependsOn(generatePluginInfo)
}
tasks.named<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>("compileKotlin") {
    dependsOn(generatePluginInfo)
}
