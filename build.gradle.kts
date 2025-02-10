import cl.franciscosolis.sonatypecentralupload.SonatypeCentralUploadTask
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.fabric.loom)
    id("cl.franciscosolis.sonatype-central-upload") version "1.0.3"
    `maven-publish`
}

group = "net.ririfa"
version = "1.0.0"

val localProperties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    minecraft(libs.minecraft)
    mappings(libs.yarn)
    modImplementation(libs.fabric.api)
    modImplementation(libs.fabric.loader)
    modApi(libs.mcef)
    modApi(libs.beacon)
    modApi(libs.classGraph)
    modApi(libs.kotlin.reflect)
}

java {
    withSourcesJar()
    withJavadocJar()

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

tasks.withType<JavaCompile> {
    options.release.set(21)
}

tasks.named<Jar>("jar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveClassifier.set("")
}

tasks.register<Jar>("genAllJar") {
    dependsOn("jar", "sourcesJar", "javadocJar")
}

publishing {
    publications {
        //maven
        create<MavenPublication>("maven") {

            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])

            pom {
                name.set("MCEF-AL")
                description.set("A MCEF abstraction layer")
                url.set("https://github.com/ririf4/mcef-al")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/license/mit")
                    }
                }
                developers {
                    developer {
                        id.set("ririfa")
                        name.set("RiriFa")
                        email.set("main@ririfa.net")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/ririf4/mcef-al.git")
                    developerConnection.set("scm:git:ssh://github.com/ririf4/mcef-al.git")
                    url.set("https://github.com/ririf4/mcef-al")
                }
                dependencies
            }
        }
    }
    repositories {
        mavenLocal()
    }
}

tasks.named<SonatypeCentralUploadTask>("sonatypeCentralUpload") {
    dependsOn("clean", "jar", "sourcesJar", "javadocJar", "generatePomFileForMavenPublication")

    username = localProperties.getProperty("cu")
    password = localProperties.getProperty("cp")

    archives = files(
        tasks.named("jar"),
        tasks.named("sourcesJar"),
        tasks.named("javadocJar"),
    )

    pom = file(
        tasks.named("generatePomFileForMavenPublication").get().outputs.files.single()
    )

    signingKey = localProperties.getProperty("signing.key")
    signingKeyPassphrase = localProperties.getProperty("signing.passphrase")
}
