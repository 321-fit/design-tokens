plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

// Group + artifact name for composite-build substitution.
// Consumers reference this module as `implementation("com.fit321:fitui")`
// — the path is rewired locally via `dependencySubstitution` in the
// consumer `settings.gradle.kts`.
group = "com.fit321"
version = "0.1.0-SNAPSHOT"

base {
    archivesName.set("fitui")
}

android {
    namespace = "com.fit321.designtokens"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    kotlin {
        sourceSets["main"].kotlin.srcDirs("src/main/kotlin")
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.compose.ui:ui:1.9.4")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.9.4")
    implementation("androidx.compose.foundation:foundation:1.9.4")
    implementation("androidx.compose.material3:material3:1.3.2")
    implementation("androidx.compose.material:material-icons-extended:1.7.6")
    implementation("androidx.compose.animation:animation:1.9.4")
    // Avatars take a URL, mirroring SwiftUI's AsyncImage in FitAvatar. Coil reads the
    // consumer app's ImageLoader, so a host that registers extra decoders keeps them.
    implementation("io.coil-kt:coil-compose:2.7.0")
}
