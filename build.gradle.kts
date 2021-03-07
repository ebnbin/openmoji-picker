buildscript {
    apply(
        if (java.util.Properties().also { it.load(File(rootDir, "local.properties").reader()) }["devEnabled"] == "true") {
            "../gradle-build/extra.gradle.kts"
        } else {
            val version = rootProject.extra["dev.gradle-build"]
            "https://raw.githubusercontent.com/ebnbin/gradle-build/$version/extra.gradle.kts"
        }
    )
    repositories {
        google()
        jcenter()
    }
    dependencies {
        val dependencyMap: Map<String, String> by rootProject.extra
        fun dependency(id: String): String {
            val version = dependencyMap[id].also {
                requireNotNull(it)
            }
            return "$id:$version"
        }
        classpath(dependency("com.android.tools.build:gradle"))
        classpath(dependency("org.jetbrains.kotlin:kotlin-gradle-plugin"))
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://jitpack.io")
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
