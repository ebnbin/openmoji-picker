plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    `maven-publish`
}

android {
    val versionMap: Map<String, String> by rootProject.extra
    compileSdkVersion(versionMap.getValue("compileSdkVersion").toInt())
    defaultConfig {
        minSdkVersion(versionMap.getValue("minSdkVersion").toInt())
        targetSdkVersion(versionMap.getValue("targetSdkVersion").toInt())
        val proguardFiles = project.file("proguard").listFiles() ?: emptyArray()
        consumerProguardFiles(*proguardFiles)
    }
    sourceSets {
        configureEach {
            val srcDirs = project.file("src/$name")
                .listFiles { file -> file.isDirectory && file.name.startsWith("res-") }
                ?: emptyArray()
            res.srcDirs(*srcDirs)
        }
    }
    resourcePrefix("openmoji_picker_")
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        moduleName = "dev.ebnbin.openmojipicker"
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    val dependencyMap: Map<String, String> by rootProject.extra
    fun dependency(id: String): String {
        val version = dependencyMap[id].also {
            requireNotNull(it)
        }
        return "$id:$version"
    }
    api("com.github.ebnbin:eb:0.0.20")
//    api(project(":eb"))
    api("com.github.ebnbin:eb-material:0.0.7")
//    api(project(":eb-material"))
    implementation(dependency("androidx.annotation:annotation"))
    implementation(dependency("androidx.lifecycle:lifecycle-viewmodel-ktx"))
    implementation(dependency("androidx.lifecycle:lifecycle-livedata-ktx"))
    implementation(dependency("androidx.activity:activity-ktx"))
    implementation(dependency("androidx.fragment:fragment-ktx"))
    implementation(dependency("androidx.coordinatorlayout:coordinatorlayout"))
    implementation(dependency("androidx.constraintlayout:constraintlayout"))
    implementation(dependency("androidx.recyclerview:recyclerview"))
    implementation(dependency("com.google.code.gson:gson"))
    implementation(dependency("com.github.bumptech.glide:glide"))
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
            }
        }
    }
}
