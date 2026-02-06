import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins{
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin{
    jvm()

    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets{
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.preview)

            implementation(projects.core)

            implementation("io.github.koalaplot:koalaplot-core:0.11.0")

            implementation(libs.materialKolor)


            implementation(libs.navigation3.ui)
            implementation(libs.compose.material3.adaptive.navigation3)
            implementation(libs.kotlinx.serialization.core)

            implementation(libs.compose.material3.adaptive)
            implementation(libs.compose.material3.adaptive.layout)

            implementation(libs.compose.material3.adaptive.navigation.suite)

            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.9.6")

        }

        jvmMain.dependencies{
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation("org.slf4j:slf4j-simple:2.0.17")
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "su.pank"
            packageVersion = "1.0.0"
        }
    }
}