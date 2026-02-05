@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin{
    jvm()
    wasmJs()

    sourceSets{
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}