// Root project for design-tokens composite build.
// Plugin versions must mirror the consumer Android app
// (321fit_android_new) — Gradle composite builds share a classloader,
// so divergent AGP/Kotlin/Compose-compiler versions cause class
// conflicts. Bump in lockstep with `321fit_android_new/gradle/libs.versions.toml`.

plugins {
    id("com.android.library") version "8.13.2" apply false
    id("org.jetbrains.kotlin.android") version "2.1.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.21" apply false
}
