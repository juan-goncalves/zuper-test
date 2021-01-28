object Versions {
    const val kotlin_stdlib = "1.4.21"
    const val coroutines = "1.4.1"
    const val android_gradle = "4.1.1"

    const val androidx_core = "1.5.0-alpha01"
    const val androidx_appcompat = "1.3.0-alpha01"
    const val androidx_activity = "1.1.0"
    const val androidx_constraint_layout = "2.0.0-rc1"
    const val androidx_lifecycle = "2.3.0-alpha05"
    const val google_material = "1.3.0-rc01"

    const val retrofit = "2.6.0"

    const val junit = "4.13.1"
    const val androidx_junit = "1.1.1"
    const val espresso = "3.3.0"
}

@Suppress("unused")
object Deps {
    const val kotlin_gradle_plugin =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin_stdlib}"
    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin_stdlib}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val android_coroutines =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val android_gradle = "com.android.tools.build:gradle:${Versions.android_gradle}"

    const val androidx_core_ktx = "androidx.core:core-ktx:${Versions.androidx_core}"
    const val androidx_appcompat = "androidx.appcompat:appcompat:${Versions.androidx_appcompat}"
    const val androidx_activity_ktx = "androidx.activity:activity-ktx:${Versions.androidx_activity}"
    const val androidx_constraint_layout =
        "androidx.constraintlayout:constraintlayout:${Versions.androidx_constraint_layout}"
    const val androidx_lifecycle_viewmodel_ktx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.androidx_lifecycle}"
    const val androidx_lifecycle_livedata_ktx =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.androidx_lifecycle}"
    const val google_material = "com.google.android.material:material:${Versions.google_material}"

    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val moshi = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"

    const val junit = "junit:junit:${Versions.junit}"
    const val androidx_junit = "androidx.test.ext:junit:${Versions.androidx_junit}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
}