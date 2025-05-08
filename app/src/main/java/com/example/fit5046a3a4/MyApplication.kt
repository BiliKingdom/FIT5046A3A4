package com.example.fit5046a3a4

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp // ✅ 告诉 Hilt 这是应用程序入口
class MyApplication : Application()
