import java.io.File

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")

    alias(libs.plugins.devtoolsKsp)
}

android {
    namespace = "com.example.presentation"
    compileSdk = 36

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
}

val generateTokensClasspath by configurations.creating

dependencies {
    implementation(project(":domain"))

    // UI - Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.activity.compose) // for 'collectAsStateWithLifecycle()'

    // UI - Material
    implementation(libs.material)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.compose.material.icons.extended)

    // UI - Image
    implementation(platform(libs.coil.bom))
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.coil.svg)
    implementation(libs.coil.video)

    // UI - Glance
    implementation(libs.androidx.glance.appwidget)

    // Lifecycle & Navigation
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    implementation(libs.androidx.navigation.compose)

    // DI
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)

    // Permissions
    implementation(libs.accompanist.permissions)

    // Firebase & Auth
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.messaging)
    implementation(libs.credential.manager)
    implementation(libs.credential.manager.google)
    implementation(libs.googleid)
    implementation(libs.kotlinx.coroutines.play.services)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    val kotlinVersion = libs.versions.kotlin.get()

    generateTokensClasspath("org.jetbrains.kotlin:kotlin-compiler-embeddable:$kotlinVersion")
    generateTokensClasspath("org.jetbrains.kotlin:kotlin-script-runtime:$kotlinVersion")
    generateTokensClasspath("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    generateTokensClasspath("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    generateTokensClasspath("org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable:$kotlinVersion")
    generateTokensClasspath("org.jetbrains.kotlin:kotlin-serialization-compiler-plugin-embeddable:$kotlinVersion")

    // (스크립트가 사용할 라이브러리 - 유지)
    generateTokensClasspath("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    generateTokensClasspath("com.squareup:kotlinpoet:1.16.0")

}

tasks.register<JavaExec>("generateDesignTokens") {

    group = "petbulance"
    description = "Generates Primitives.kt from tokens.json using kts script."

    // --- 입력/출력 (기존과 동일) ---
    val scriptFile = project.rootProject.file("tokens/generateColors.kts")
    val tokenFile = project.rootProject.file("tokens/tokens.json")
    inputs.file(scriptFile)
    inputs.file(tokenFile)

    val outputDir = file("src/main/java/com/example/presentation/component/theme")
    outputs.dir(outputDir)

    val gradleJavaHome = System.getProperty("org.gradle.java.home")
    if (gradleJavaHome != null && File(gradleJavaHome).exists()) {
        executable = File(gradleJavaHome, "bin/java").absolutePath
    }

    mainClass.set("org.jetbrains.kotlin.cli.jvm.K2JVMCompiler")
    classpath = generateTokensClasspath

    // 🚨 2. (핵심 수정)
    // args 리스트를 만들기 *전에* 플러그인 JAR 경로를 찾습니다.
    // (이 코드는 'configuration time' 경고를 발생시키지만, 지금은 유일한 해결책입니다.)
    val serializationPluginJar = generateTokensClasspath.files.first {
        it.name.startsWith("kotlin-serialization-compiler-plugin-embeddable")
    }.absolutePath

    // 6. 실행기가 스크립트를 컴파일하기 위한 인자
    args = listOf(
        "-no-stdlib",
        "-no-reflect",
        "-Xplugin=$serializationPluginJar",
        "-classpath",
        generateTokensClasspath.asPath,
        "-script",
        scriptFile.path,
        outputDir.absolutePath,
        tokenFile.absolutePath
    )

    // 7. (권장) 인코딩 문제 방지
    jvmArgs = listOf("-Dfile.encoding=UTF-8")
}

// 8. (필수) 빌드 파이프라인에 태스크 연결
tasks.named("preBuild") {
    dependsOn(tasks.named("generateDesignTokens"))
}