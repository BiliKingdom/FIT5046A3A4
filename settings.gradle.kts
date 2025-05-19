pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io") // ✅ 添加 JitPack 支持插件解析（如有插件来自 JitPack）
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io") // ✅ 添加 MapLibre 库所需仓库
    }
}

rootProject.name = "FIT5046A3A4"
include(":app")
