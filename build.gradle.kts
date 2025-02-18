val riderPlatformVersionProp = prop("riderPlatformVersion")
val ideaPlatformVersionProp = prop("ideaPlatformVersion")
val pluginSinceBuildProp = prop("pluginSinceBuild")
val pluginUntilBuildProp = prop("pluginUntilBuild")
val projectType = System.getenv("IDE_TYPE") ?: "IC"

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
    idea
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    //id("org.jetbrains.intellij") version "1.17.4"
    id("org.jetbrains.intellij.platform") version "2.2.1"
}

    group = "com.example"
    version = "1.0-SNAPSHOT"

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

    dependencies {
        intellijPlatform {
            if (projectType == "RD") {
                create("RD", riderPlatformVersionProp, useInstaller = false)
            } else {
                create("IC", ideaPlatformVersionProp, useInstaller = false)
            }
            bundledPlugins("com.intellij.java", "JUnit")
        }
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
        implementation("com.google.code.gson:gson:2.12.1")
        testImplementation("com.intellij.remoterobot:remote-robot:0.11.23")
        testImplementation ("com.intellij.remoterobot:remote-fixtures:0.11.23")

    }


    tasks {
        // Set the JVM compatibility versions
        withType<JavaCompile> {
            sourceCompatibility = "19"
            targetCompatibility = "19"
        }
        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions.jvmTarget = "17"
        }

        patchPluginXml {
            sinceBuild.set(pluginSinceBuildProp)
            if (pluginUntilBuildProp.isNotEmpty())
                untilBuild.set(pluginUntilBuildProp)

        }

        signPlugin {
            certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
            privateKey.set(System.getenv("PRIVATE_KEY"))
            password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
        }

        publishPlugin {
            token.set(System.getenv("PUBLISH_TOKEN"))
        }
    }


tasks.test {
    useJUnitPlatform()
}


fun File.isPluginJar(): Boolean {
    if (!isFile) return false
    if (extension != "jar") return false
    return zipTree(this).files.any { it.isManifestFile() }
}

fun File.isManifestFile(): Boolean {
    if (extension != "xml") return false
    val rootNode = try {
        val parser = groovy.xml.XmlParser()
        parser.parse(this)
    } catch (e: Exception) {
        logger.error("Failed to parse $path", e)
        return false
    }
    return rootNode.name() == "idea-plugin"
}


fun prop(key: String) = extra.properties[key] as? String
    ?: error("Property `$key` is not defined in gradle.properties")
