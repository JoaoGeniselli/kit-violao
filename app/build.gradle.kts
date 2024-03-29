plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = 33
    defaultConfig {
        applicationId = "com.dosei.music.scoreconverter"
        minSdk = 21
        targetSdk = 33
        versionCode = 7
        versionName = "1.3.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val secrets = SecretsAPI.forProject(rootProject)

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            resValue("string", "admob_application_id", secrets.getString(SecretsKeys.adMobAppId))
            resValue("string", "admob_home_banner_id", BuildConstants.adMobFakeBannerId)
            resValue("string", "admob_transposer_banner_id", BuildConstants.adMobFakeBannerId)
            resValue("string", "admob_dictionary_banner_id", BuildConstants.adMobFakeBannerId)
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            resValue(
                "string",
                "admob_application_id",
                secrets.getString(SecretsKeys.adMobAppId)
            )
            resValue(
                "string",
                "admob_home_banner_id",
                secrets.getString(SecretsKeys.adMobHomeBannerId)
            )
            resValue(
                "string",
                "admob_transposer_banner_id",
                secrets.getString(SecretsKeys.adMobTransposerBannerId)
            )
            resValue(
                "string",
                "admob_dictionary_banner_id",
                secrets.getString(SecretsKeys.adMobDictionaryBannerId)
            )
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compiler.kotlin.ext.get()
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    packagingOptions {
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
    }
    namespace = "com.dosei.music.scoreconverter"
}

kotlin {
    jvmToolchain(8)
}

dependencies {
    implementation(project(path = ":ui"))
    implementation(project(path = ":player"))

    implementation(libs.kotlin.stdlib)
    implementation(libs.app.compat)
    implementation(libs.core.ktx)
    implementation(libs.constraint.layout)
    implementation(libs.recycler.view)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics.core)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.play.services.ads)
    implementation(libs.android.material)
    implementation(libs.lifecycle.runtime)

    //region UI

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.graphics)
    implementation(libs.compose.preview)
    implementation(libs.activity.compose)

    debugImplementation(libs.compose.tooling.ui)
    debugImplementation(libs.compose.test.manifest)

    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.android.test.ext)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.compose.junit)

    //endregion

    implementation(libs.hilt.compose)
    implementation(libs.navigation.compose)
    implementation(libs.ktransposer)
    implementation(libs.arpeggio)

    testImplementation(libs.junit.core)

    androidTestImplementation(libs.android.test.ext)
}
