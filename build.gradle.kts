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
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.0.20"
    id("org.jetbrains.intellij.platform") version "2.4.0"
}

group = "org.jyu"
version = "1.0.3"

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
            bundledPlugins("com.intellij.java", "JUnit")
        }

    }
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("com.google.code.gson:gson:2.12.1")
    testImplementation("com.intellij.remoterobot:remote-robot:0.11.23")
    testImplementation ("com.intellij.remoterobot:remote-fixtures:0.11.23")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
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
        if (pluginUntilBuildProp.isNotEmpty())
            untilBuild.set(pluginUntilBuildProp)

    }
    // Plugin signing might be important later so these are only commented out
    /*
    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }*/
}


tasks.test {
    useJUnitPlatform()
}


// From https://gitlab.jyu.fi/tie/tools/comtest.intellij/-/blob/master/build.gradle.kts
fun prop(key: String) = extra.properties[key] as? String
    ?: error("Property `$key` is not defined in gradle.properties")
