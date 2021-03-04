plugins {
    kotlin("jvm")
}

dependencies {
    val dependencyMap: Map<String, String> by rootProject.extra
    fun dependency(id: String): String {
        val version = dependencyMap[id].also {
            requireNotNull(it)
        }
        return "$id:$version"
    }

    implementation(dependency("com.google.code.gson:gson"))
}
