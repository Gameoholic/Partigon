plugins {
    kotlin("jvm") version "1.9.10"

    `maven-publish`
//    id("maven-publish")

    id("io.papermc.paperweight.userdev") version "1.5.5"
    id("xyz.jpenilla.run-paper") version "2.1.0" // Adds runServer and runMojangMappedServer tasks for testing

    // Shades and relocates dependencies into our plugin jar. See https://imperceptiblethoughts.com/shadow/introduction/
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.github.gameoholic"
version = "1.0.1"
description = "Partigon."
val apiVersion = "1.20"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}


dependencies {
    implementation(kotlin("stdlib-jdk8"))

    paperweight.paperDevBundle("1.20.1-R0.1-SNAPSHOT") //the paper dev bundle is a compile-only dependency, paper itself provides it. No need to shade

    implementation("net.objecthunter", "exp4j","0.4.8")
    implementation("org.apache.commons:commons-math3:3.6.1")
}




tasks {
    // Configure reobfJar to run when invoking the build task
    assemble {
        dependsOn(reobfJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
        // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
        // See https://openjdk.java.net/jeps/247 for more information.
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
        val props = mapOf(
            "name" to project.name,
            "version" to project.version,
            "description" to project.description,
            "apiVersion" to apiVersion
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    shadowJar {
        // helper function to relocate a package into our package
        fun reloc(pkg: String) = relocate(pkg, "${project.group}.${project.name}.dependency.$pkg")

        reloc("net.objecthunter.exp4j")
        //reloc("stdlib-jdk8")
    }
}


publishing {
    publications {
        create<MavenPublication>("partigon") {
//            groupId = "test"
//            artifactId = "test"
//            version = "test"

            from(components["kotlin"])
            artifact(tasks["kotlinSourcesJar"])
        }
    }
}

//
//publishing {
//    publications {
//        create<MavenPublication>("partigon") {
//            from(components["kotlin"])
//            artifact(tasks["kotlinSourcesJar"])
//        }
//    }
//}
