plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt) apply false // ✅ 添加 Hilt 插件（项目级别可见）
}

buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
    }
}
