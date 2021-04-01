buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Dependencies.comAndroidToolsBuild_gradle.notation("7.0.0-alpha12"))
        classpath(Dependencies.orgJetbrainsKotlin_kotlinGradlePlugin.notation("1.4.31"))
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven("https://jitpack.io")
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
