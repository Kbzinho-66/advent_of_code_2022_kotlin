plugins {
    kotlin("jvm") version "1.9.0"
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
    }

    wrapper {
        gradleVersion = "8.2.1"
    }
}
