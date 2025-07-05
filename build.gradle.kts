import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.intellij.platform") version "2.6.0"
}

group = "com.gha-run-syntax-support"
version = "0.2"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        create("IU", "2025.1")
        bundledPlugin("org.jetbrains.plugins.yaml")

        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
        testBundledPlugins("com.intellij.modules.json", "com.jetbrains.sh", "org.jetbrains.plugins.github", "org.toml.lang")
        testPlugin("PythonCore", "251.26927.70")
    }

    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.25.1")
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "251"
        }

        changeNotes = """
      Initial version
    """.trimIndent()
    }
}

tasks {
    withType<JavaCompile>().configureEach {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }

    withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }
}
