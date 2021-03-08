plugins {
    kotlin("jvm")
}

dependencies {
    fun dependency(id: String): String {
        val dependencyMap: Map<String, String> by rootProject.extra
        return "$id:${dependencyMap.getValue(id)}"
    }
    implementation(dependency("com.google.code.gson:gson"))
}
