plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.tiborszabo.quizapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tiborszabo.quizapp"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}



dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)

    //This option should be commented for code compilation, but uncommented for Javadoc generation.
    //implementation(fileTree(mapOf("dir" to "C:\\Users\\szabo\\AppData\\Local\\Android\\Sdk\\platforms\\android-34", "include" to listOf("*.aar", "*.jar"), "exclude" to listOf(""))))

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

