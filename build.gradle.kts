import xyz.jpenilla.runpaper.task.RunServer
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar


plugins {
    java
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("com.gradleup.shadow") version "9.4.0"
}

group = project.property("group") as String
version = project.property("version") as String




val targetMcVersion = project.property("target") as String

val mcVersionArray = targetMcVersion.split(".").map(String::toInt)

fun isOlder(ver1: List<Int>, ver2: List<Int>): Boolean {
    // is version 1 older than version 2
    // pad the version to include zeroes
    fun padVersion(v: List<Int>, length: Int = 3): List<Int> {
        return List(length) { i -> v.getOrElse(i) { 0 } }
    }

    val ver1p = padVersion(ver1)
    val ver2p = padVersion(ver2)

    for (i in 0..2) {
        if (ver1p[i] != ver2p[i]) return ver1p[i] < ver2p[i]
    }
    return false // versions are equal
}

repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "JHaaC"
        url = uri("https://maven.pkg.github.com/EvilSquirrelGuy/JHaaC")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GPR_USER")
            password = project.findProperty("gpr.token") as String? ?: System.getenv("GPR_TOKEN")
        }
    }
}

dependencies {
    // yes.
    // implementation("org.spongepowered:configurate-xml:4.2.0")
    // Paper API
    compileOnly("io.papermc.paper:paper-api:${targetMcVersion}-R0.1-SNAPSHOT")
    // Compilation libs
    // dynamic link
    compileOnly(files("lib/SocialCredit_v1.6.2.jar"))
    // needed in final
    implementation("org.jsoup:jsoup:1.22.1")
    implementation("dev.evilsquirrelguy.jhaac:jhaac:0.1.1")
}



tasks {
    named<RunServer>("runServer") {
        minecraftVersion(targetMcVersion)
    }

    jar {
        if (!isOlder(mcVersionArray, listOf(1, 20, 5))) {
            manifest {
                attributes(
                    "paperweight-mappings-namespace" to "mojang"
                )
            }
        }
    }

    // only works if shadow plugin is applied
    shadowJar {
        if (!isOlder(mcVersionArray, listOf(1, 20, 5))) {
            manifest {
                attributes(
                    "paperweight-mappings-namespace" to "mojang"
                )
            }
        }
    }
}


val targetJavaVersion = 21

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion

    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"

    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
        options.release.set(targetJavaVersion)
    }
}

tasks.processResources {
    // properties for the plugin.json
    val props = mapOf(
        "version" to version,
        "name" to project.property("name") as String,
        "description" to project.property("description") as String,
        "website" to project.property("website") as String,
        "authors" to (project.property("authors") as String).trim('[', ']').split(","),
        "contributors" to (project.property("contributors") as String).trim('[', ']').split(","),
        "main" to project.property("main") as String,
        "prefix" to project.property("prefix") as String,
        "api_version" to targetMcVersion,
    )
    inputs.properties(props)
    filteringCharset = "UTF-8"

    filesMatching("plugin.yml") {
        expand(props)
    }
}