@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
    application
}

version = "1.0-SNAPSHOT"
group = "com.quebin31"

application {
    mainClass.set("com.quebin31.aoc.ApplicationKt")
}

dependencies {
    implementation(libs.clikt)

    testImplementation(kotlin("test"))
}

tasks.test.configure {
    useJUnitPlatform()
}

tasks.compileKotlin.configure {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}
