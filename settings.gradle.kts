include("plugin")
include("common")
include(
    "idea",
    "rider"
)


pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "TIDE-JetBrains"